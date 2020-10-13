example daffodil validator plugin
===

Provides Schematron validation with [ph-schematron](https://github.com/phax/ph-schematron)

### test spi integration

1. Export the path to the test [data](data) dir as `data_dir` to the environment

2. From the root of daffodil; stage the cli package
`sbt daffodil-cli/universal:stage`

3. From `daffodil-cli/target/universal/stage`; run the application, verifying it fails as expected due to missing schematron validator jar
`./bin/daffodil parse --schema $data_dir/bmp.dfdl.xsd --validate sch=$data_dir/bmp.sch $data_dir/MARBLES.BMP`
Should result in
`[error] Bad arguments for option 'validate': 'sch=/sample/data/bmp.sch' - Unrecognized ValidationMode sch=/sample/data/bmp.sch.  Must be 'on', 'limited', 'off', or name of spi validator.`

4. From the root of schematron validator; create an assembly jar
`sbt assembly`

5. From `daffodil-schematron-validator/target/scala-2.12`; copy the validator jar to the staged daffodil-cli application lib dir `daffodil-cli/target/universal/stage/lib`

6. From `daffodil-cli/target/universal/stage`; run the application
`./bin/daffodil parse --schema $data_dir/bmp.dfdl.xsd --validate sch=$data_dir/bmp.sch $data_dir/MARBLES.BMP`

7. Enjoy the parsed BMP and schematron validation status.


### dev
missing some generated sources when using the project refs from the daffodil repo, just copy those over for now
- `daffodil-lib/src/main/scala/org/apache/daffodil/xsd`
- `daffodil-lib/src_managed/main/org/apache/daffodil/schema/annotation/props/gen`
- `daffodil-lib/src_managed/main/org/apache/daffodil/api/DaffodilTunablesGen.scala`
- `daffodil-lib/src_managed/main/org/apache/daffodil/api/WarnIdGen.scala`
