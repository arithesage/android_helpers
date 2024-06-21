#!/usr/bin/env python

import os
import sys

"""
To be sure that we use the Python modules included in '_scripts' folder,
and no other, we clear this process's PYTHONPATH environment variable
and set it to include the '_scripts' folder.
"""

os.environ ["PYTHONPATH"] = ""
sys.path.append (os.path.join (os.getcwd(), "_scripts"))

from filesystem import append_to_file, file_contains, make_path, replace_all_in
from os_utils import si_path
from shell import cp, echo, echo_va, mkdir, mv
from stdio import ask_if_continue
from str_utils import str_empty



class DATA:
    TOMLAndroidLibraryEntry = \
    "androidLibrary = { id = \"com.android.library\", version.ref = \"agp\" }"

    def __init__(self) -> None:
        pass


class MESSAGES:
    Aborted = "Aborted."
    AlreadyInitialized = "Android module already initialized."
    Done = "Done!"
    InitializationFailed = "Module initialization failed."
    Failed = "Failed!"

    def __init__ ():
        pass


class PLACEHOLDERS:
    PROJECT_NAME_PLACEHOLDER = "BaseNativeModule"
    PACKAGE_PLACEHOLDER = "me.android.modules"

    def __init__() -> None:
        pass


class PATHS:
    Project = "./"
    BaseModule = make_path (Project, "_required", PLACEHOLDERS.PROJECT_NAME_PLACEHOLDER)
    BaseModuleSrcMainJava = make_path (BaseModule, "/src/main/java")
    BaseModuleSrcAndroidTestJava = make_path (BaseModule, "/src/androidTest/java")
    BaseModuleSrcTestJava = make_path (BaseModule, "/src/test/java")
    Gradle = make_path (Project, "gradle")

    def __init__ ():
        pass


class FILES:
    AndroidTest_JAVA = "ExampleInstrumentedTest.java"
    Build_KTS = "build.gradle.kts"
    LibsVersions_TOML = "libs.versions.toml"
    CMakeLists_TXT = "CMakeLists.txt"
    NativeLib_CPP = "nativelib.cpp"
    NativeLib_JAVA = "NativeLib.java"
    Settings_KTS = "settings.gradle.kts"
    Test_JAVA = "ExampleUnitTest.java"

    def __init__ ():
        pass


class ModulePaths:
    Root = ""
    SrcMainCPP = ""
    SrcMainJava = ""
    SrcAndroidTestJava = ""
    SrcTestJava = ""

    def __init__ () -> None:
        pass

    @staticmethod
    def Init (module_name):
        ModulePaths.Root = make_path (".", module_name)

        ModulePaths.SrcMainCPP = make_path (ModulePaths.Root,
                                           "/src/main/cpp")

        ModulePaths.SrcMainJava = make_path (ModulePaths.Root, 
                                            "/src/main/java")
        
        ModulePaths.SrcAndroidTestJava = make_path (ModulePaths.Root, 
                                                    "/src/androidTest/java")
        
        ModulePaths.SrcTestJava = make_path (ModulePaths.Root, 
                                             "/src/test/java")


def abort ():
    echo (MESSAGES.Aborted)
    echo ("")
    exit (1)


def usage ():
    echo ("Creates a new Android module in this project.")
    echo ("Usage: add_module <module name> <project package>")
    echo ("")


def init_module (module_name, package_name):
    module_package_dirtree = si_path (package_name.replace (".", "/"))

    if str_empty (module_package_dirtree):
        echo ("ERROR: Failed creating package path.")
        abort ()

    ModulePaths.Init (module_name)
    
    androidtest_src = make_path (ModulePaths.SrcAndroidTestJava, module_package_dirtree, FILES.AndroidTest_JAVA)
    libjava_src = make_path (ModulePaths.SrcMainJava, module_package_dirtree, FILES.NativeLib_JAVA)
    libversions_toml = make_path (PATHS.Gradle, FILES.LibsVersions_TOML)
    settings_kts = make_path (PATHS.Project, FILES.Settings_KTS)
    test_src = make_path (ModulePaths.SrcTestJava, module_package_dirtree, FILES.Test_JAVA)

    echo ("Ready for creating Android native module")
    echo ("----------------------------------------")
    echo_va ("Module name: $[0]", module_name)
    echo_va ("Package: $[0]", package_name)
    echo ("----------------------------------------")

    proceed = ask_if_continue ()

    if not proceed:
        abort ()


    echo ("Creating module tree...")

    if not cp (PATHS.BaseModule, ModulePaths.Root):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()

    if not mkdir (make_path (ModulePaths.SrcMainJava, module_package_dirtree)) \
    or not mkdir (make_path (ModulePaths.SrcAndroidTestJava, module_package_dirtree)) \
    or not mkdir (make_path (ModulePaths.SrcTestJava, module_package_dirtree)):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()

    if not mv (make_path (ModulePaths.SrcAndroidTestJava, FILES.AndroidTest_JAVA), androidtest_src) \
    or not mv (make_path (ModulePaths.SrcTestJava, FILES.Test_JAVA), test_src) \
    or not mv (make_path (ModulePaths.SrcMainJava, FILES.NativeLib_JAVA), libjava_src):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()

    echo (MESSAGES.Done)


    echo_va ("Updating $[0] and $[1] ...", FILES.AndroidTest_JAVA, FILES.Test_JAVA)

    if not replace_all_in (PLACEHOLDERS.PACKAGE_PLACEHOLDER, package_name, androidtest_src) \
    or not replace_all_in (PLACEHOLDERS.PACKAGE_PLACEHOLDER, package_name, test_src):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()



    echo_va ("Updating $[0] ...", FILES.Build_KTS)

    if not replace_all_in (PLACEHOLDERS.PACKAGE_PLACEHOLDER, package_name, make_path (ModulePaths.Root, FILES.Build_KTS)):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()

    echo (MESSAGES.Done)


    echo_va ("Updating $[0] ...", FILES.Settings_KTS)

    if not append_to_file (settings_kts, "\ninclude(\":" + module_name + "\")\n"):
        echo_va ("ERROR: Failed updating $[0].", settings_kts)
        echo_va ("Append manually the module entry: include(\"" + module_name + "\")") 

    echo (MESSAGES.Done)


    echo_va ("Updating $[0] ...", FILES.LibsVersions_TOML)

    if not file_contains (libversions_toml, \
                          DATA.TOMLAndroidLibraryEntry):
        
        if not append_to_file (libversions_toml, \
                               DATA.TOMLAndroidLibraryEntry):
            echo (MESSAGES.Failed)
            echo_va ("You'll need to append $[0] yourself to $[1].",
                     DATA.TOMLAndroidLibraryEntry,
                     libversions_toml)
            
        echo (MESSAGES.Done)
    


if __name__ == "__main__":
    argv = sys.argv
    argc = len (sys.argv[1:])

    if argc != 2:
        usage ()
        exit (1)

    init_module (argv[1], argv[2])
    
