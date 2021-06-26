<?xml version="1.0" encoding="UTF-8"?>
<exe4j version="6.0.2" transformSequenceNumber="2">
    <directoryPresets config="${projectPath}/target" />
    <application name="${artifactId}" distributionSourceDir="${projectPath}/dist">
        <languages>
            <principalLanguage id="en" customLocalizationFile="" />
        </languages>
    </application>
    <executable name="${artifactId}" type="2" iconSet="true" iconFile="${projectPath}/dist/icon.ico" executableDir="." redirectStderr="false" stderrFile="error.log" stderrMode="append" redirectStdout="false" stdoutFile="output.log" stdoutMode="append" failOnStderrOutput="true" executableMode="1" changeWorkingDirectory="true" workingDirectory="." singleInstance="true" serviceStartType="2" serviceDependencies="" serviceDescription="" jreLocation="" executionLevel="asInvoker" checkConsoleParameter="true" globalSingleInstance="false" singleInstanceActivate="true" dpiAware="java9+" amd64="true">
        <messageSet>
            <message id="ErrorDialogCaption" text="Ok" />
        </messageSet>
        <versionInfo include="true" fileVersion="0.1" fileDescription="" legalCopyright="" internalName="" productName="" companyName="www.kancy.com" productVersion="0.1" />
    </executable>
    <splashScreen show="false" width="0" height="0" bitmapFile="" textOverlay="false">
        <text>
            <statusLine x="20" y="20" text="" fontSize="8" fontColor="0,0,0" bold="false" />
            <versionLine x="20" y="40" text="version %VERSION%" fontSize="8" fontColor="0,0,0" bold="false" />
        </text>
    </splashScreen>
    <java mainClass="${packageName}.Application" mainMode="1" vmParameters="-Dtray=true -Dtray.console=true -Dtray.system.properties=true" arguments="" allowVMPassthroughParameters="true" preferredVM="" bundleRuntime="true" minVersion="1.8" maxVersion="" allowBetaVM="false" jdkOnly="false">
        <searchSequence>
            <registry />
            <envVar name="JAVA_HOME" />
            <envVar name="JDK_HOME" />
        </searchSequence>
        <classPath>
            <archive location="${projectPath}/target/${artifactId}.jar" failOnError="false" />
        </classPath>
        <modulePath />
        <nativeLibraryDirectories />
        <vmOptions>
            <options version="11" line="--add-opens java.base/jdk.internal.loader=ALL-UNNAMED --add-opens jdk.zipfs/jdk.nio.zipfs=ALL-UNNAMED" />
            <options version="9" line="--add-opens java.base/jdk.internal.loader=ALL-UNNAMED --add-opens jdk.zipfs/jdk.nio.zipfs=ALL-UNNAMED" />
            <options version="10" line="--add-opens java.base/jdk.internal.loader=ALL-UNNAMED --add-opens jdk.zipfs/jdk.nio.zipfs=ALL-UNNAMED" />
            <options version="12" line="--add-opens java.base/jdk.internal.loader=ALL-UNNAMED --add-opens jdk.zipfs/jdk.nio.zipfs=ALL-UNNAMED" />
        </vmOptions>
    </java>
    <includedFiles />
    <unextractableFiles />
</exe4j>
