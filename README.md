# BoreHost

BoreHost is a Paper/Folia plugin that exposes your Minecraft server through a Bore reverse tunnel.

It lets players connect to your server from the internet without manual router port forwarding.

## What This Plugin Adds

- Creates a reverse tunnel from your local Minecraft server port to a Bore server.
- Assigns or reserves a public remote port (`remote-port` or random with `0`).
- Logs a public endpoint your players can join.

## Why You Might Want It

- Host quickly without opening router ports or setting up complex NAT rules.
- Useful for temporary servers, testing, and small private sessions.
- Minimal setup: install plugin, set host/port/secret, restart.

## Critical Information Before Downloading

- Requires a reachable Bore server (`server-host`) that you control or trust.
- This plugin is tunnel transport only; it is not an anti-DDoS or security plugin.
- If Bore connection fails, the plugin disables itself until config/network is fixed.

## Features

- Reverse tunnel from a Bore server to your local Minecraft server
- Supports dynamic remote ports (`remote-port: 0`)
- Optional shared secret authentication
- Folia support enabled
- Lightweight setup with simple config

## Installation

1. Build or download `BoreHost-<version>.jar`.
2. Place the jar in your server `plugins/` folder.
3. Start the server once to generate `plugins/BoreHost/config.yml`.
4. Edit config values.
5. Restart the server.

```yaml
# Bore server host
server-host: "bore.pub"
# 0 = random remote port
remote-port: 0
# If secret is needed, set it here. Otherwise, leave it empty.
secret: ""
```

### Config options

- `server-host`: Bore server hostname or IP.
- `remote-port`: Public port on the Bore server. Use `0` for a random available port.
- `secret`: Optional tunnel secret. Leave empty if your Bore server does not require one.

## Usage

After startup, BoreHost connects automatically and logs the public endpoint:

`Public remote endpoint: <server-host>:<assigned-port>`

Share that host and port with players.

## How to self-host a Bore server
Please refer to the official Bore documentation for instructions on setting up your own Bore server:
https://github.com/ekzhang/bore

## Build From Source

```bash
mvn clean package
```

The built jar will be in `target/`.

## License

MIT. See [LICENSE](LICENSE).
