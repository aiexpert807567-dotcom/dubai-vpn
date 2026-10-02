#!/usr/bin/env bash
# Dubai VPN - environment check (LOCAL/CODESPACES)

check() {
  if command -v "$1" >/dev/null 2>&1; then
    echo "OK       $1  ->  $($2 2>&1 | head -n 1)"
  else
    echo "MISSING  $1"
  fi
}

echo "== Dubai VPN environment check =="
check git "git --version"
check python3 "python3 --version"
check pip3 "pip3 --version"
check java "java -version"
check node "node --version"
check gh "gh --version"

echo
echo "== Project structure =="
for d in mobile backend/app backend/tests server docs scripts; do
  if [ -d "$d" ]; then echo "OK       $d/"; else echo "MISSING  $d/"; fi
done
for f in README.md .gitignore .env.example; do
  if [ -f "$f" ]; then echo "OK       $f"; else echo "MISSING  $f"; fi
done

echo
echo "== Secret safety =="
if git check-ignore -q .env; then
  echo "OK       .env is ignored by Git"
else
  echo "WARNING  .env is NOT ignored by Git"
fi
