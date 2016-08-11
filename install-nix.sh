#! /bin/bash

#Vars for checks
EXE=false
ICON=false
USERDESKTOP=false
LOCALDESKTOP=false

#Notify user about sudo
echo "Sudo is required to copy the executable file to /usr/bin"

#Copy executable to bin
sudo cp bin/decred /usr/bin/
sudo cp bin/DecredWallet.jar /usr/bin/

#Copy icon to icon folder
sudo cp build-resources/decred.png /usr/share/icons/ 

#Create .desktop file
sudo cp build-resources/Decred.desktop /usr/share/applications/ 

#Create .local desktop file
cp build-resources/Decred.desktop $HOME/.local/share/applications/


#Check if the install worked
if [ -f "/usr/bin/decred" ]; then 
	EXE=true 
fi

if [ -f "/usr/share/icons/decred.png" ]; then 
	ICON=true 
fi

if [ -f "/usr/share/applications/Decred.desktop" ]; then 
	USERDESKTOP=true 
fi

if [ -f "$HOME/.local/share/applications/Decred.desktop" ]; then 
	LOCALDESKTOP=true 
fi

if [ $EXE == true ] && [ $ICON == true ] && [ $USERDESKTOP == true ] && [ $LOCALDESKTOP == true ]; then
	echo "Install successful."
else
	echo "Install failed."
fi

#Debug info for user
echo ""
echo "All values below should be true"
echo "Executable: "$EXE
echo "Icon: "$ICON
echo "Usr Desktop: "$USERDESKTOP
echo "Local Desktop: "$LOCALDESKTOP
