#! /bin/bash

ant jar

#Copy embed script
cp build-resources/embed.sh bin/decred

#Move into bin folder
cd bin

#Append jar to executable shell
uuencode DecredWallet.jar DecredWallet.jar >> decred

#Set permissions
chmod +x DecredWallet.jar
chmod +x decred

#Manually use Launch4j to create Windows executable


#Check all went well
if [ -f "DecredWallet.jar" ]; then 
	echo ""
	echo "Successfully built the Decred Wallet GUI."
else
	echo ""
	echo "Build failed, post your error to the Thread on Decred Fourms for help."
fi
