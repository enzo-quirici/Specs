[![Java](https://img.shields.io/badge/Java-17%2F21-blue.svg?logo=java)](https://adoptium.net/) [![FreeBSD](https://img.shields.io/badge/FreeBSD-supported-red.svg?logo=freebsd)](https://www.freshports.org/java/openjdk17/) [![GhostBSD](https://img.shields.io/badge/GhostBSD-supported-3f5cff.svg?logo=ghost)](https://www.ghostbsd.org/) [![Linux](https://img.shields.io/badge/Linux-supported-green.svg?logo=linux)](https://openjdk.java.net/) [![macOS](https://img.shields.io/badge/macOS-supported-lightgrey.svg?logo=apple)](https://adoptium.net/) [![Windows](https://img.shields.io/badge/Windows-supported-blue.svg?logo=windows)](https://adoptium.net/) [![Arch Linux](https://img.shields.io/badge/Arch-Linux-blue.svg?logo=arch-linux)](https://archlinux.org/packages/?q=openjdk) [![Debian](https://img.shields.io/badge/Debian-supported-a80030.svg?logo=debian)](https://packages.debian.org/search?keywords=openjdk) [![Fedora](https://img.shields.io/badge/Fedora-supported-294172.svg?logo=fedora)](https://src.fedoraproject.org/rpms/java-17-openjdk) [![Gentoo](https://img.shields.io/badge/Gentoo-supported-54487a.svg?logo=gentoo)](https://packages.gentoo.org/packages/dev-java/openjdk)

---

# Specs :

### A Java application that displays detailed information about your PC's hardware.

![img.png](img.png)

# Features :

**CPU Details :** View your processor's model, physical and logical core count.  
**GPU Information :** See your graphics processor name and VRAM capacity.  
**RAM Usage :** Shows total, used, and free memory, without cached files.  
**Operating System :** Displays OS name and version.  
**Auto Refresh :** Set intervals to automatically update displayed information.

# Minimum Requirements :

🖥️ OS : Windows 7 or better / Linux Kernel version 4.4 or better / Mac OS 10.11 or better / Free BSD Based OS 12 or better  
⚙️ CPU : 64 bits CPU  
💾 RAM : 512 MO of RAM  
💿 Storage : 512 Mo of free space  
☕ Java : JDK 17 or better

# Project Status :

### Legend :
- ✅ Yes
- ❌ No
- ⚠️ Partial or Special Case
- 🟧 Not Available/Unknown

| OS           | Launch    | Installer | Standalone Version   | Icon | Stress Test | OS | CPU  | RAM  | GPU     |
|--------------|-----------|-----------|----------------------|------|-------------|----|------|------|---------|
| Windows      | ✅        | ✅        | ✅                  | ✅   | ✅          | ✅ | ✅  | ✅   | ✅    |
| Arch Linux   | ✅        | ✅        | ✅                  | ✅   | ✅          | ✅ | ✅  | ✅   | ✅    |
| Ubuntu       | ✅        | ✅        | ✅                  | ✅   | ✅          | ✅ | ✅  | ✅   | ✅    |
| Debian       | ✅        | ✅        | ✅                  | ✅   | ✅          | ✅ | ✅  | ✅   | ✅    |
| Fedora       | ✅        | ✅        | ✅                  | ✅   | ✅          | ✅ | ✅  | ✅   | ✅    |
| Gentoo Linux | ✅        | ✅        | ✅                  | ✅   | ✅          | ✅ | ✅  | ✅   | ✅    |
| Mac OS       | ✅        | ✅        | ✅                  | ✅   | ✅          | ✅ | ✅  | ✅   | ✅    |
| BSD          | ✅        | ❌        | ❌                  | ⚠️   | ✅          | ✅ | ✅  | ✅   | ⚠️    |

# Validation :

- the validation feature is a feature that allows you to display information from multiple PCs to create statistics.
![img_1.png](img_1.png)
- This feature requires a server to be used.  
- currently there is no official server available, but you can create one by using the guide [how to create you own Specs Server](https://github.com/enzo-quirici/Specs-Server/).

# dependency :

## libjpeg turbo 8 :

This is a dependency that may be necessary to install the .deb file on certain Linux distributions based on Debian.

### Debian :

```bash
wget http://mirrors.kernel.org/ubuntu/pool/main/libj/libjpeg-turbo/libjpeg-turbo8_2.1.2-0ubuntu1_amd64.deb  
sudo apt install ./libjpeg-turbo8_2.1.2-0ubuntu1_amd64.deb
```

## glxinfo :  

- GLXINFO has been replaced with OSHI GLXINFO is now optional.  

- To enable GPU and VRAM information retrieval on Linux, this program requires `glxinfo`. Below are the instructions for installing `glxinfo` on Debian, Ubuntu, Fedora, Arch Linux, and Gentoo.  

### Debian / Ubuntu :
On Debian or Ubuntu, `glxinfo` is part of the `mesa-utils` package :
```bash
sudo apt-get update
sudo apt-get install mesa-utils
```
### Fedora :
On Fedora, you can install glxinfo with the mesa-demos package :
```bash
sudo dnf install mesa-demos
```
### Arch Linux :
On Arch Linux, glxinfo is provided by the mesa-demos package :
```bash
sudo pacman -S mesa-demos
```
### Gentoo :
On Gentoo, you can install glxinfo by emerging the mesa-progs package :
```
sudo emerge --ask mesa-progs -av
```
### Verifying the Installation :
To confirm that glxinfo is installed correctly, run :
```bash
glxinfo | grep "OpenGL version"
```
If glxinfo returns OpenGL version information, the installation was successful.

MIT License

Copyright (c) 2025 [Enzo Quirici]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
