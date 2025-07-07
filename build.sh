export MUSL_HOME=$PWD/musl-toolchain
export CFLAGS="-I$MUSL_HOME/include"
export LDFLAGS="-L$MUSL_HOME/lib"
export PATH="$MUSL_HOME/bin:$PATH"

mvn clean package -DskipTests -Pnative -s ~/.m2/empty-settings.xml
