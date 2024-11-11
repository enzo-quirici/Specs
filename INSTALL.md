# Installation guide for the 1.2 Version :
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
curl https://github.com/enzo-quirici/Specs/releases/download/V1.2/Specs-Arch-Linux-1.2.zip
```
- Decompress the archive :
``` Bash
bsdtar -xf Specs-1.2-Arch-Linux.zip
```
- Go to the extracted file :
``` Bash
cd Specs-Arch-Linux
```
- Install the app :
``` Bash
makepkg -si
```