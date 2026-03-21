# KFS Space Invaders

Klasická hra Space Invaders — klon v retro pixel-art stylu. Dedikace **pro Kubu**.

## Tech Stack

- **Java 17** + **libGDX 1.14.0**
- **gdx-teavm 1.4.0** — web build (Java → JavaScript)
- Vlastni ECS framework (Entity, Component, System, World)
- Proceduralne generovane textury, zvuky i hudba

## Jak to vypada

- 480x640 portrait rozliseni
- 11x5 formace alienu (3 typy: 10/20/30 bodu)
- 4 stitove bunkry (klasicky obracene U)
- Mystery ship kazdych 15-30s (50-300 bodu)
- Wave system — kazda vlna je rychlejsi
- 3 chiptune hudebni tracky, 4 retro zvukove efekty

## Ovladani

- **Sipky / A,D** — pohyb doleva/doprava
- **Mezernik / Sipka nahoru** — strelba
- **Touch/Mysh** — pohyb + strelba (web/mobile)

## Build

### Pozadavky

- JDK 17+
- Gradle wrapper je soucasti projektu

### Desktop

```bash
./gradlew lwjgl3:run
```

### Web (TeaVM)

```bash
./gradlew teavm:build
```

Vysledek je v `teavm/build/dist/webapp/` — staci nahrat na webovy server.

### Lokalni web server

```bash
./gradlew teavm:run
# http://localhost:8080/
```

## CI/CD

Push na `main` automaticky:

1. Buildne TeaVM web verzi
2. Zabali do `spaceinvaders-web.tar.gz`
3. Vytvori GitHub Release

## Deploy na server

Na cilovem serveru stahne posledni release z GitHubu a rozbal do adresare:

```bash
./deploy-space-invaders.sh              # default: ~/www/kuba/gm006
./deploy-space-invaders.sh /jina/cesta  # custom adresar
```

Potrebuje `curl` na serveru (standardne k dispozici).

## Struktura projektu

```
core/       — hlavni kod hry (ECS, komponenty, systemy, screeny)
lwjgl3/     — desktop launcher
teavm/      — web launcher + TeaVM builder
assets/     — fonty, UI skin, zvuky, hudba, ikony
```

## Licence

Soukromy projekt pro Kubu.
