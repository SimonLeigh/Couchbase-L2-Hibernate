# -*- mode: ruby -*-
# vi: set ft=ruby :

$script = <<SCRIPT
echo I am provisioning...
date > /etc/vagrant_provisioned_at
SCRIPT

Vagrant.configure("2") do |config|
  config.vm.provision "shell", inline: $script
  config.vm.provider "virtualbox" do |v|
    v.memory = 1024
    v.cpus = 4
  end
end

Vagrant::Config.run do |config|
  config.vm.box = "precise64"
  config.vm.box_url = "http://files.vagrantup.com/precise64.box"

  config.vm.share_folder "bootstrap", "/mnt/bootstrap", "."
  config.vm.share_folder "sql", "/mnt/sql", "sql"
  config.vm.provision :shell, :path => "Vagrant-setup/bootstrap.sh"
  
  # PostgreSQL Server port forwarding
  config.vm.forward_port 5432, 15432
  config.vm.forward_port 11210, 11210
  config.vm.forward_port 11211, 11211
  config.vm.forward_port 11212, 11212
  config.vm.forward_port 8088, 8088
  config.vm.forward_port 8091, 8091
  config.vm.forward_port 8092, 8092
  config.vm.forward_port 18091, 18091
  config.vm.forward_port 18092, 18092
  config.vm.forward_port 11214, 11214
  config.vm.forward_port 11215, 11215
  
  for i in 21100..21299
    config.vm.forward_port i, i
  end
  
end
