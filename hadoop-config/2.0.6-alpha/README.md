Hadoop Configuration
====================

Single-node pseudo-distributed cluster
--------------------------------------

Modify $HADOOP_INSTALL/etc/hadoop/hadoop-env.sh

modify line 27: `export JAVA_HOME=${JAVA_HOME}`

to be:

    export JAVA_HOME=<path to your Java install home>

On Ubuntu 13.04 with Java 1.6 it would be:

    export JAVA_HOME="/usr/lib/jvm/java-6-openjdk-amd64"

Copy the following files from the `hadoop-config/2.0.6-alpha/etc/hadoop` 
directory of this repo:
```
  core-site.xml
  hdfs-site.xml
  mapred-site.xml
  yarn-site.xml
```
  to:
```
  $HADOOP_INSTALL/etc/hadoop
```

This is a barebones configuration that stores any data files in your
temp directory but it allows you to quickly get up and running executing
mapreduce jobs with mr2 and yarn.

You can uncomment the config properties that specifies specific location 
for hadoops local directories. Just edit the file system locations.
