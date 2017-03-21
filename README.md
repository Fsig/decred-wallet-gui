# decred-wallet
Decred Wallet GUI is a front end built in Java using the Canvas with an OpenGL pipeline for rendering graphics.
All components are made using Graphics2D and images where required, this gives us full flexibility to create anything we want without the limitations of swing or other frameworks.

Calls to DCRD and DCRWALLET are made via RPC, if the specified address is localhost the GUI will start the DCRD and DCRWALLET process if they are not already running.
Certificates are stored within a custom Keystore with a generated password that is added to your settings.conf.

#### Index:
* [Downloads](#download-binaries)  
* [Features](#features)
* [Build and install on Linux](#build-and-install-on-linux)
* [Build on Windows](#build-on-windows)
* [Help](#help)

___

#### Download binaries
* [Release page](https://github.com/Fsig/decred-wallet-gui/releases)

___

#### Features

* Multithreaded
* Render control
  * When the window is out of focus it will reduce the update rate
  * This means the process will use at little resources as possible
* RPC 
  * All calls are now made using RPC with SSL
  * Connect to DCRD and DCRWALLET outside of local network
* Connect to specified address
  * Imports local certificates automatically
  * Import custom certificates manually
* Cached results
  * Realtime updates on actions
* Accounts
  * View all
  * Add accounts
  * Rename accounts
* Transactions
  * Listed by account
  * Transaction type
  * Sent / Received
* Contacts
  * Add unlimited
  * Remove
  * Copy details to clipboard
* Balance
  * View total by account
  * View spendable by account
  * View pending by account
  * View locked by account
* Stake mining
  * View stake info
  * View tickets
  * Ticket purchase
  * Get public key for pool
  * Import script for pool
* Send coins
  * Send from specified account
  * Send to contact
  * Suggestions when typing in to field
* Receive coins
  * Get new address
  * View all current addresses per account
  * Click to copy address
* Logs
  * Daemon log
  * Wallet log
  * Gui log
* Language
  * Supports multiple languges though conf files
* Security
  * Dump private key
  * Change passphrase
  * Get master public key
* Network 
  * Network info
  * Peers list
  * Disconnect peers
  
___

#### Build and install on Linux

Building and installing has been tested on Debian based Linux.

###### Requirements
* JDK 1.8 or greater
* ANT: http://ant.apache.org/ or manual JAR packaging

###### Install
* Open a terminal in repo root
* Give the build and install scripts execute permissions **chmode +x *.sh**
* Run build **./build-nix.sh**
* Check results, should see **Sucessfully built the Decred Wallet GUI.**
* Run install **./install-nix.sh**
* Check all the returned results are **true**

___

#### Build on Windows

Building has been tested on Windows 7.

###### Requirements
* JDK 1.8 or greater
* ANT: http://ant.apache.org/ 

###### Create Jar
* Open a command prompt in repo root and run command "ant"
* Check results, should see **BUILD SUCCESSFUL**
* The .jar file can be found in bin/

###### Create EXE wrapper
* Open a command prompt in repo root and run command "ant exe"
* Check results, should see **BUILD SUCCESSFUL**
* The .exe file can be found in bin/

___

#### Help

* [Support/Suggestions thread](https://forum.decred.org/threads/decred-wallet-gui.1119/)
* [Email](mailto::fsig@hmamail.com)
* [JDK Download](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Set environment variable Windows](http://www.tutorialspoint.com/java/java_environment_setup.htm)


**Donations welcome: DsmcWt82aeraJ22bayUtMXm8dyRL8bFnBVY**
