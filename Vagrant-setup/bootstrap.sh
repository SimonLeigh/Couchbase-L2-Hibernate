#!/bin/sh -e

COUCHBASEADMIN=Administrator
COUCHBASEADMINPASS=password
BUCKETNAME=l2-cache

# Edit the following to change the name of the database user that will be created:
APP_DB_USER=thumbtack_user
APP_DB_PASS=12345

# Edit the following to change the name of the database that is created (defaults to the user name)
APP_DB_NAME=thumbtack_db

# Edit the following to change the version of PostgreSQL that is installed
PG_VERSION=9.3

###########################################################
# Changes below this line are probably not necessary
###########################################################
print_db_usage () {
  echo "Your PostgreSQL database has been setup and can be accessed on your local machine on the forwarded port (default: 15432)"
  echo "  Host: localhost"
  echo "  Port: 15432"
  echo "  Database: $APP_DB_NAME"
  echo "  Username: $APP_DB_USER"
  echo "  Password: $APP_DB_PASS"
  echo ""
  echo "Admin access to postgres user via VM:"
  echo "  vagrant ssh"
  echo "  sudo su - postgres"
  echo ""
  echo "psql access to app database user via VM:"
  echo "  vagrant ssh"
  echo "  sudo su - postgres"
  echo "  PGUSER=$APP_DB_USER PGPASSWORD=$APP_DB_PASS psql -h localhost $APP_DB_NAME"
  echo ""
  echo "Env variable for application development:"
  echo "  DATABASE_URL=postgresql://$APP_DB_USER:$APP_DB_PASS@localhost:15432/$APP_DB_NAME"
  echo ""
  echo "Local command to access the database via psql:"
  echo "  PGUSER=$APP_DB_USER PGPASSWORD=$APP_DB_PASS psql -h localhost -p 15432 $APP_DB_NAME"
}

create_tables() {
  TABLES_CREATED=/etc/vm_tables_created_on_timestamp
  
  if [ ! -f "$TABLES_CREATED"  ]; then
    echo "tables NO created"
  else
    echo "tables was created"
    if [ $(date -r $TABLES_CREATED +%s) -lt $(date -r /mnt/sql/tables.sql +%s) ]; then
      echo "sql changed"
    else
      echo "sql NO changed"
    fi
  fi

  NEED_CREATE_TABLES=false

  if [ ! -f "$TABLES_CREATED"  ]; then
    NEED_CREATE_TABLES=true
  elif [ $(date -r $TABLES_CREATED +%s) -lt $(date -r /mnt/sql/tables.sql +%s) ]; then
    NEED_CREATE_TABLES=true
  fi

  if ("$NEED_CREATE_TABLES" && true); then
    PGUSER=$APP_DB_USER PGPASSWORD=$APP_DB_PASS psql -f /mnt/sql/tables.sql -h localhost $APP_DB_NAME
    echo "Tables have recreated"
    date > "$TABLES_CREATED"
  fi
}



export DEBIAN_FRONTEND=noninteractive

PROVISIONED_ON=/etc/vm_provision_on_timestamp
if [ -f "$PROVISIONED_ON" ]
then
  echo "VM was already provisioned at: $(cat $PROVISIONED_ON)"
  echo "To run system updates manually login via 'vagrant ssh' and run 'apt-get update && apt-get upgrade'"
  echo ""
  create_tables
  print_db_usage
  exit
fi

PG_REPO_APT_SOURCE=/etc/apt/sources.list.d/pgdg.list
if [ ! -f "$PG_REPO_APT_SOURCE" ]
then
  # Add PG apt repo:
  echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" > "$PG_REPO_APT_SOURCE"

  # Add PGDG repo key:
  wget --quiet -O - http://apt.postgresql.org/pub/repos/apt/ACCC4CF8.asc | apt-key add -
fi

# Update package list and upgrade all packages
apt-get update
apt-get -y upgrade

apt-get -y install "postgresql-$PG_VERSION" "postgresql-contrib-$PG_VERSION"

PG_CONF="/etc/postgresql/$PG_VERSION/main/postgresql.conf"
PG_HBA="/etc/postgresql/$PG_VERSION/main/pg_hba.conf"
PG_DIR="/var/lib/postgresql/$PG_VERSION/main"

# Edit postgresql.conf to change listen address to '*':
sed -i "s/#listen_addresses = 'localhost'/listen_addresses = '*'/" "$PG_CONF"

# Append to pg_hba.conf to add password auth:
echo "host    all             all             all                     md5" >> "$PG_HBA"

# Restart so that all new config is loaded:
service postgresql restart

cat << EOF | su - postgres -c psql
-- Create the database user:
CREATE USER $APP_DB_USER WITH PASSWORD '$APP_DB_PASS';

-- Create the database:
CREATE DATABASE $APP_DB_NAME WITH OWNER $APP_DB_USER;
EOF

create_tables
print_db_usage
# Tag the provision time:
date > "$PROVISIONED_ON"

echo "Installation couchbase"
wget http://packages.couchbase.com/releases/3.1.5/couchbase-server-enterprise_3.1.5-ubuntu12.04_amd64.deb -nv
echo "Package is got"
dpkg -i couchbase-server-enterprise_3.1.5-ubuntu12.04_amd64.deb
#dpkg -i /mnt/bootstrap/couchbase-server-enterprise_2.5.1_x86_64.deb
echo "Couchbase installed"
sleep 10
echo "Init node"
/opt/couchbase/bin/couchbase-cli node-init -u Administrator -p password -c localhost:8091 --node-init-data-path=/tmp
echo "Node inited"
sleep 10
echo "Init cluster"
/opt/couchbase/bin/couchbase-cli cluster-init -u Administrator -p password -c localhost:8091 --cluster-init-username="$COUCHBASEADMIN" --cluster-init-password="$COUCHBASEADMINPASS" --cluster-init-port=8091 --cluster-init-ramsize=512
echo "Cluster inited"
sleep 10
echo "Creat bucket"
/opt/couchbase/bin/couchbase-cli bucket-create -u Administrator -p password -c localhost:8091 --bucket="$BUCKETNAME" --bucket-type=couchbase --bucket-ramsize=500 --bucket-replica=1 --enable-flush=1 -u Administrator -p password
echo "bucket created"
echo "Administrator: login=$COUCHBASEADMIN, password=$COUCHBASEADMINPASS"
echo "bucket name:$BUCKETNAME"
echo "url: localhost:8091"
echo "Couchbase created"

sudo reboot
#echo "Successfully created PostgreSQL dev virtual machine."
#echo "Install memcached..."
#sudo apt-get install memcached
#sudo sed -i 's/-l 127.0.0.1/#-l 127.0.0.1/g' /etc/memcached.conf
#echo "Install memcached ended"
#echo ""
#print_db_usage