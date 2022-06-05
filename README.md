## Development setup ##
From project root directory, run:

``` bash
./scripts/install-git-hook-pre-commit.sh
```
to setup `git` pre-commit hook. This hook calls the custom `sbt` command `preCommit`, defined in `build.sbt`.
Call `git commit` with the `--no-verify` argument to override calling the pre-commit hook.

## Ammonite support ##
In `sbt`, from any module:
```bash
Test / run
```
will launch the `ammonite` REPL with the current module on its classpath.
