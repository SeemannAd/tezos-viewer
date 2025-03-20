![Platform: Android](https://img.shields.io/badge/platform-android-green)
![GitHub Actions](https://img.shields.io/badge/github-actions-purple)
[![Tezos Badge](https://img.shields.io/badge/gitlab-tezos-blue)](https://gitlab.com/tezos/tezos)
<div style="text-align: center; margin-top: 30px; margin-bottom: 30px;">
    <img src="https://cryptologos.cc/logos/tezos-xtz-logo.png?v=040" alt="Tezos Logo" style="width: 10%; height: auto;"/>
<h1>Tezos Viewer</h1>
</div>

This is a project that allows you to view the state of the Tezos blockchain.

Data provided by [TzKT API](https://api.tzkt.io).

## Overview

The architecture is build by a agile domain driven approach. The project follows the SOLID developer patter and tries to reduce 
dependencies to third party libraries and focus in core functionality. 

Please see [Architecture Vision](documention/202503_Tezos_Viewer_Architecture_Vision.pdf)

Current Tech-Stack:
1. Jetpack Compose
2. NavHost 
3. Ktor - api client
4. ModernStorage - google`s fileSystem storage
5. Mockk - Test Framework for mocking data

### Domains

The is divided into different domains. Each domain represents a stakeholder epic.  

#### Blocks
Overview of the current state of the Tezos Blockchain. The latest blocks for the current cycle 
are displayed. 

#### Block
Details of a selected Block within the Blockchain. 

#### Settings
Access user settings like adding Api-Key for PRO access to the Tezos blockchain.