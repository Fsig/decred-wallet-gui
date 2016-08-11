#! /bin/sh
rm -f DecredWallet.jar
uudecode $0
java -Djava.net.preferIPv4Stack=true -jar DecredWallet.jar
rm -f DecredWallet.jar
exit
