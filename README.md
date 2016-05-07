# IdeaConcordionSupport [![Build Status](https://travis-ci.org/concordion/idea-concordion-support.svg)](https://travis-ci.org/concordion/idea-concordion-support)

This plugin provides support for [concordion](http://concordion.org/) framework.

It can be installed from [Intellij IDEA plugins repository](https://plugins.jetbrains.com/plugin/7978)

**Settings > Plugins > Browse repositories...** > search for *Concordion support*

![demo](https://plugins.jetbrains.com/files/7978/screenshot_15835.png)

- [How does **Markdown support** integration look like?](https://plugins.jetbrains.com/files/7978/screenshot_15837.png)
- [How does **Markdown** integration look like?](https://plugins.jetbrains.com/files/7978/screenshot_15836.png)

It is built by gradle so it does not require to have intellij idea plugin sdk set up to start development.

# Requirements

- idea 14.1+ with JUnit plugin running on jdk 8 (project may use any jdk)

# Supported features

- Autocompletion
  - concordion commands in HTML and MD specs
  - test fixture members in concordion expressions
  - spec variables
- Navigation between spec and test fixture
  - with *Navigate between concordion spec and fixture* (**Ctrl + Shift + s**)
  - clicking concordion icon in test fixture and spec
  - using *go to declaration* (**Ctrl + left click** on concordion expression)
- Find usages (**Ctrl + F7**) list will include usages of methods and properties in concordion spec (for HTML specs only)
- Renaming support (for HTML specs only)
  - it is possible to rename method/field from its usage in spec
  - after renaming method/field corresponding usage in spec will be renamed as well
  - concordion variable will be renamed in spec along with all its usages
- Run configuration (**Ctrl + Shift + F10**) from HTML or MD spec. JUnit for corresponding test fixture will be created and started
