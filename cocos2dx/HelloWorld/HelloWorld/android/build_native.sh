#!/bin/bash
# set params
PROJECT_NAME=HelloWorld
NDK_ROOT_LOCAL=~/android-ndk-r7
COCOS2DX_ROOT_LOCAL=/Volumes/MacData/github/dum/cocos2dx/HelloWorld

# try to get global variable
if [ $NDK_ROOT"aaa" != "aaa" ]; then
    echo "use global definition of NDK_ROOT: $NDK_ROOT"
    NDK_ROOT_LOCAL=$NDK_ROOT
fi

if [ $COCOS2DX_ROOT"aaa" != "aaa" ]; then
    echo "use global definition of COCOS2DX_ROOT: $COCOS2DX_ROOT"
    COCOS2DX_ROOT_LOCAL=$COCOS2DX_ROOT
fi

PROJECT_ROOT=$COCOS2DX_ROOT_LOCAL/$PROJECT_NAME/android

# make sure assets is exist
if [ -d $PROJECT_ROOT/assets ]; then
    rm -rf $PROJECT_ROOT/assets
fi

mkdir $PROJECT_ROOT/assets

# copy resources
for file in $COCOS2DX_ROOT_LOCAL/$PROJECT_NAME/Res/*
do
    if [ -d $file ]; then
        cp -rf $file $PROJECT_ROOT/assets
    fi

    if [ -f $file ]; then
        cp $file $PROJECT_ROOT/assets
    fi
done

# remove image_rgba4444.pvr.gz
rm -f $PROJECT_ROOT/assets/Images/image_rgba4444.pvr.gz

#build
pushd $NDK_ROOT_LOCAL
./ndk-build -C $PROJECT_ROOT $*
popd

