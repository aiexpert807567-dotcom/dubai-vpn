# Dubai VPN

A personal Android VPN app with one-tap WireGuard connection and an independent
AdGuard Home DNS ad-blocking switch.

> A VPN encrypts traffic between your device and the VPN server. It does not
> guarantee complete anonymity.

## Status

Phase 1 of 17: architecture and repository setup.

## Architecture

```text
Android app ──HTTPS──► Backend (FastAPI) ──internal──► AdGuard Home
     │
     └──WireGuard UDP tunnel──► VPN server ──► Internet
```

- The VPN tunnel never passes through the backend.
- Ad blocking is independent of the VPN: turning it off disables filtering only.
- The phone generates its own WireGuard private key; only the public key is sent
  to the server.

## Technology stack

| Part | Choice |
|---|---|
| Mobile app | Native Kotlin + Jetpack Compose |
| VPN | WireGuard via Android VpnService |
| Backend | Python, FastAPI, Pydantic, SQLAlchemy, SQLite, Uvicorn |
| DNS filtering | AdGuard Home (internal API only) |
| Server | Ubuntu VM (Oracle Cloud Always Free, to be verified) |
| Tests | pytest (backend), Android test tools (mobile) |

## Repository layout

```text
mobile/    Android app (Kotlin)
backend/   FastAPI backend
server/    Server setup and firewall scripts (PRODUCTION SERVER)
docs/      Architecture, deployment, security, troubleshooting
scripts/   Helper scripts
```

## Local development (GitHub Codespaces)

Codespaces is the development environment only. It is NOT the production VPN server.

Check your environment:

```bash
bash scripts/check-env.sh
```

## Environment variables

Copy `.env.example` to `.env` and fill in values. Never commit `.env`.

## Security rules

- No secrets, private keys, tokens, or signing keys in Git.
- AdGuard admin API is never exposed publicly.
- All sensitive backend endpoints require authentication.

## Roadmap

See the 17-phase plan: architecture, app skeleton, UI, backend, auth, WireGuard
server, app integration, AdGuard, AdGuard toggle, devices, QR, stats, hardening,
end-to-end tests, Oracle deployment, APK/AAB build, production testing.

## Not an anonymity product

Dubai VPN is a general-purpose personal VPN and DNS filter. It does not provide
anonymity and includes no stealth or obfuscation features.
