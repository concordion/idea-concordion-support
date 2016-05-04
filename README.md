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

- Autocomplete with spec variables
- Autocomplete with concordion commands
- Autocomplete with members of the fixture
- Autocomplete chains with members
- Navigation between concordion html spec and test fixture using concordion icon and navigation action (ctrl + shift + s)
- Ctrl+click navigation and usages search for concordion expressions
- Quick fields and methods creation from usages
- Renaming support
- Run concordion spec from HTML file
