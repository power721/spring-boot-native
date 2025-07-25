export MUSL_HOME=$PWD/musl-toolchain
export CFLAGS="-I$MUSL_HOME/include"
export LDFLAGS="-L$MUSL_HOME/lib"

sudo rm -rf musl-* zlib-*

curl -O https://musl.libc.org/releases/musl-1.2.5.tar.gz
curl -O https://zlib.net/fossils/zlib-1.2.13.tar.gz

tar -xzvf musl-1.2.5.tar.gz
pushd musl-1.2.5
./configure --prefix=$MUSL_HOME --static
sudo make && make install
popd

rm -rf musl-1.2.5 musl-1.2.5.tar.gz

ln -s $MUSL_HOME/bin/musl-gcc $MUSL_HOME/bin/x86_64-linux-musl-gcc

export PATH="$MUSL_HOME/bin:$PATH"
x86_64-linux-musl-gcc --version

tar -xzvf zlib-1.2.13.tar.gz
pushd zlib-1.2.13
CC=musl-gcc ./configure --prefix=$MUSL_HOME --static
make && make install
popd

rm -rf zlib-1.2.13 zlib-1.2.13.tar.gz
