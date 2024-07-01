# [WIP] Android helpers

A collection of classes to make Android development more comfortable
(for me at least).

The helpers are singletons because many times (if not all) you need to
pass the current application context and doing this everytime you call
a functions...

So, i decided to make them singletons.

For example: If you want to show a Message, you first initialize the
DialogsHelpers, passing the current application context.

Now you access all the needed functions through DialogHelpers.Get(), and
all of them will have access to the application context.


