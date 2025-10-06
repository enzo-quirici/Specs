# Installation guide for the 2.0 Version :
## Debian / Fedora Based Distro (GUI) :
- Download the file for you distro in the release page of the project :  
  https://github.com/enzo-quirici/Specs/releases/  
**For Debian based distro chose the Debian version.**  
**For Fedora based distro chose the Fedora version.**
- Right click on the .deb / .rpm and open it with the application installer.
- on the application installer click on install.
## Arch Linux (CLI) :
- Go to the Download folder :
``` Bash
cd ~/Downloads
```
- Copy the installer file :
``` Bash
curl https://github.com/enzo-quirici/Specs/releases/download/V2.0/Specs-2.0-Arch-Linux.zip
```
- Decompress the archive :
``` Bash
bsdtar -xf Specs-2.0-Arch-Linux.zip
```
- Go to the extracted file :
``` Bash
cd Specs
```
- Install the app :
``` Bash
makepkg -si
```
## Gentoo Linux (CLI) :
- Go to the Download folder :
``` Bash
cd ~/Downloads
```
- Copy the installer file :
``` Bash
curl https://github.com/enzo-quirici/Specs/releases/download/V2.0/Specs-2.0-Gentoo-Linux.zip
```
- Decompress the archive :
``` Bash
bsdtar -xf Specs-2.0-Gentoo-Linux.zip
```
- Go to the extracted file :
``` Bash
cd Specs
```
- Add permission to the app :
``` Bash
sudo chmod +x Install.sh
```
- Install the app :
``` Bash
sudo ./Install.sh
```
