#.bashrc

# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi

TASK=""
INPUT=""
take_input() {
	TASK=$1
	INPUT=$2
}



PROJECT="/home/xchen116/opensource-repo-analyzer"
HDLIB="/usr/hdp/2.4.0.0-169/hadoop/*:/usr/hdp/2.4.0.0-169/hadoop-mapreduce/*"

# User specific aliases and functions
alias task=take_input
alias comp='rm -r out/*; unzip lib/json-mapreduce-1.0.jar -d out/; rm -r out/META-INF; javac -cp $PROJECT/lib/*:$HDLIB -d out/ src/$TASK/*.java src/StringUtils.java'
alias mkjar='jar cvf task.jar -C out/ .'
alias run='hadoop jar task.jar $TASK /user/$INPUT /user/output'
alias clean='hdfs dfs -rm -r /user/output'
alias show='hdfs dfs -cat /user/output/part*'
