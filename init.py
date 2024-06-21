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

from filesystem import file_exists, make_path, replace_all_in
from os_utils import si_path
from shell import basename, echo, echo_va, mkdir, mv, realpath
from stdio import ask_if_continue
from str_utils import str_empty




class MESSAGES:
    Aborted = "Aborted."
    AlreadyInitialized = "Android Project already initialized."
    Done = "Done!"
    InitializationFailed = "Project initialization failed."
    Failed = "Failed!"
    Finished = "All done!"

    def __init__ ():
        pass


class PATHS:
    App = "app"
    AndroidTest_JAVA = "app/src/androidTest/java"
    Main = "app/src/main"
    Main_JAVA = "app/src/main/java"
    NightValues = "app/src/main/res/values-night"
    Project = "."
    Test_JAVA = "app/src/test/java"
    Values = "app/src/main/res/values"

    def __init__ ():
        pass


class PLACEHOLDERS:
    PROJECT_NAME_PLACEHOLDER = "BaseApp"
    PACKAGE_PLACEHOLDER = "me.android.baseapp"

    def __init__(self) -> None:
        pass


class FILES:
    AndroidManifest_XML = "AndroidManifest.xml"
    AndroidTest_JAVA = "ExampleInstrumentedTest.java"
    Build_KTS = "build.gradle.kts"
    Main_JAVA = "MainActivity.java"
    Settings_KTS= "settings.gradle.kts"
    Strings_XML = "strings.xml"
    Test_JAVA = "ExampleUnitTest.java"
    Themes_XML = "themes.xml"

    def __init__ ():
        pass




def abort ():
    echo (MESSAGES.Aborted)
    echo ("")
    exit (1)


def check_if_already_initialized ():
    if file_exists (si_path (make_path (PATHS.Main_JAVA, FILES.Main_JAVA))):
        echo (MESSAGES.AlreadyInitialized)
        echo ()
        exit (1)


def usage ():
    echo ("Initializes this Android project.")
    echo ("Usage: init <project package>")
    echo ("")


def init_project (package_name):
    project_name = basename (realpath ("."))
    project_name_lowercase = project_name.lower ()
    project_package = package_name
    project_package_dirtree = si_path (project_package.replace (".", "/"))

    if str_empty (project_package_dirtree):
        echo ("ERROR: Failed creating package path.")
        abort ()

    main_src = make_path (PATHS.Main_JAVA, project_package_dirtree, FILES.Main_JAVA)
    androidtest_src = make_path (PATHS.AndroidTest_JAVA, project_package_dirtree, FILES.AndroidTest_JAVA)
    test_src = make_path (PATHS.Test_JAVA, project_package_dirtree, FILES.Test_JAVA)

    echo ("Ready for initialize Android project")
    echo ("------------------------------------")
    echo_va ("Project name: $[0]", project_name)
    echo_va ("Package: $[0]", project_package)
    echo ("------------------------------------")

    proceed = ask_if_continue ()

    if not proceed:
        abort ()


    echo ("Creating project tree...")

    if not mkdir (make_path (PATHS.Main_JAVA, project_package_dirtree)) \
    or not mkdir (make_path (PATHS.AndroidTest_JAVA, project_package_dirtree)) \
    or not mkdir (make_path (PATHS.Test_JAVA, project_package_dirtree)):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()

    if not mv (make_path (PATHS.Main_JAVA, FILES.Main_JAVA), main_src) \
    or not mv (make_path (PATHS.AndroidTest_JAVA, FILES.AndroidTest_JAVA), androidtest_src) \
    or not mv (make_path (PATHS.Test_JAVA, FILES.Test_JAVA), test_src):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()

    echo (MESSAGES.Done)


    echo_va ("Updating $[0], $[1] and $[2] ...", FILES.Main_JAVA, FILES.AndroidTest_JAVA, FILES.Test_JAVA)

    if not replace_all_in (PLACEHOLDERS.PACKAGE_PLACEHOLDER, project_package, main_src) \
    or not replace_all_in (PLACEHOLDERS.PACKAGE_PLACEHOLDER, project_package, androidtest_src) \
    or not replace_all_in (PLACEHOLDERS.PACKAGE_PLACEHOLDER, project_package, test_src):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()


    echo_va ("Updating $[0] and $[1] ...", FILES.Settings_KTS, FILES.Build_KTS)

    if not replace_all_in (PLACEHOLDERS.PROJECT_NAME_PLACEHOLDER, project_name, make_path (PATHS.Project, FILES.Settings_KTS)) \
    or not replace_all_in (PLACEHOLDERS.PACKAGE_PLACEHOLDER, project_package, make_path (PATHS.App, FILES.Build_KTS)):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()

    echo (MESSAGES.Done)


    echo_va ("Updating $[0] ...", FILES.AndroidManifest_XML)

    if not replace_all_in (PLACEHOLDERS.PROJECT_NAME_PLACEHOLDER, project_name, make_path (PATHS.Main, FILES.AndroidManifest_XML)):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()

    echo (MESSAGES.Done)


    echo_va ("Updating $[0] ...", FILES.Themes_XML)

    if not replace_all_in (PLACEHOLDERS.PROJECT_NAME_PLACEHOLDER, project_name, make_path (PATHS.Values, FILES.Themes_XML)) \
    or not replace_all_in (PLACEHOLDERS.PROJECT_NAME_PLACEHOLDER, project_name, make_path (PATHS.NightValues, FILES.Themes_XML)):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()

    echo (MESSAGES.Done)

    
    echo_va ("Updating $[0] ...", FILES.Strings_XML)

    if not replace_all_in (PLACEHOLDERS.PROJECT_NAME_PLACEHOLDER, project_name, make_path (PATHS.Values, FILES.Strings_XML)):
        echo (MESSAGES.Failed)
        echo (MESSAGES.InitializationFailed)
        abort ()

    echo (MESSAGES.Done)

    echo (MESSAGES.Finished)
    echo ()




if __name__ == "__main__":
    argv = sys.argv
    argc = len (sys.argv[1:])

    if argc != 1:
        usage ()
        exit (1)

    init_project (argv[1])
    
