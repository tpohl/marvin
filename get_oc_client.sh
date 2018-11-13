#!/bin/sh

echo 'Downloading oc client'
wget https://github.com/openshift/origin/releases/download/v3.9.0/openshift-origin-server-v3.9.0-191fece-linux-64bit.tar.gz -P /home/user/
echo 'Unpacking oc client'
tar xfvz /home/user/openshift-origin-server-v3.9.0-191fece-linux-64bit.tar.gz -C /projects
mv  /projects/openshift-origin-server-v3.9.0-191fece-linux-64bit /projects/ocpclient
echo 'Adding oc to Path'
export PATH=$PATH:/projects/ocpclient/
echo 'Testing oc client'
oc
echo 'Removing tar file'
rm /home/user/openshift-origin-server-v3.9.0-191fece-linux-64bit.tar.gz
echo 'Done'