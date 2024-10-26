# JC++ Bridge Connect C++ - Java `@Bridge`.

BridgeGenerator is a Java utility designed to automate the generation of JNI (Java Native Interface) bridge code. It scans specified packages for classes containing methods annotated with `@Bridge`, generates corresponding Java files, compiles them, and produces native header files.

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
  - [Annotation Definition](#annotation-definition)
  - [Running the Generator](#running-the-generator)
  - [Generated Files](#generated-files)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- **Class Scanning**: Automatically scans specified packages for Java classes.
- **Method Annotation**: Generates Java files for methods annotated with `@Bridge`.
- **Compilation**: Compiles generated Java files to create JNI header files.
- **Output Management**: Automatically creates the necessary directory structure for generated files.

## Requirements

- Java Development Kit (JDK) 8 or higher.
- Basic understanding of Java and JNI.
- An existing annotation definition for `@Bridge`.

## Installation

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/yourusername/BridgeGenerator.git
   cd BridgeGenerator


## MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

1. The above copyright notice and this permission notice shall be included in all
   copies or substantial portions of the Software.

2. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
   SOFTWARE.
