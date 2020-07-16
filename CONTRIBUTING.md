# Contributing Guidelines
First of all, thank you for your interest in contributing to this project. 
To make your contribution as easy as possible, this document provides you with all relevant information to help you
 get your contribution accepted.

Following contributions are welcome:
  1. Reporting a bug.
  2. Requesting new features / improvements.
  3. Contribute code.
 
Do you think anything important is missing? Do not hesitate to get in touch with us via our support address shown
 below or just open an Issue.

## How to report a bug or request features/improvements
You found a bug or have a good idea for a new feature or improvement? Perfect! Just to open an Issue as described in
 this [section](#Issues).
 
If you like to contribute suitable code as well, take a look at the next 
[section](#how-to-contribute-a-patch-or-feature). 
                            
## How to contribute a patch or feature
If you like to contribute a patch or feature to our project please consider the follow steps:

1. Identify or create the related issue.
2. Follow the steps described in [Contributing code](#contributing-code)
3. Submit a pull request

## General 

### Security vulnerabilities
If you find a security vulnerability, do NOT open an issue. Email our support address instead, to give us time fixing
the bug before it's getting public.

### Code Conventions and Housekeeping
None of these is essential for a pull request, but they will all help.  They can also be
added after the original pull request but before a merge.

* Coding Guidelines: We think our code should have a consistent look and feel and make it look like it comes from a
 single source, thus we follow the [Google Style Guide for Java](https://google.github.io/styleguide/javaguide.html).
* Code-Style: To make sure new code does conform to the Coding Guideline we're using the Maven Checkstyle plugin. To
make it as easy as possible for you following the Guideline we recommend to import a corresponding file containing the
code styles into your preferred IDE. Checkout the Google [repository](https://github.com/google/styleguide) containing
files for the most common IDEs (e.g. Eclipse or IntelliJ). 
* To support generating a helpful code documentation, please add some Javadoc comments to your code (a good reference can 
be found [here](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html#styleguide)).
* Add a license header comment to all new .java files (copy from existing files in the project)
* A few unit tests would help a lot as well -- someone has to do it.

### Working with the code
Working with the code is pretty straightforward, as you may know from other projects. Just in case you need help
 setting up your local environment, we describe some steps to make it as easy as possible for you.
 
#### Clone the code
At first, checkout the code to your local system: 

```cmd
git clone https://github.com/msg-systems/graphql-io-server-java
```
 
 
#### Build from source
To build this project from source code, just execute the following Maven command from the projects root folder (make
 sure you have Maven installed on your system):
```cmd
mvn clean compile 
``` 
 
#### Import the code into your IDE
Next step is to import the code as a project into your preferred IDE. Since there are a number of IDEs on the market, 
we recommend that you familiarize yourself with how to perform this step. Usually you will find detailed instructions
 in the corresponding documentation.

Please make sure that you also configure the code styles for this project in your IDE to take advantage of the
 automatic code formatting support that most IDEs provide. The code styles are stored in file _xyz_ in the projects
  root folder.

### Issues
Issues are used as the primary method for tracking anything to do with this project.

### Issue Lifecycle

The issue lifecycle is mainly driven by the core maintainers, but is good information for those
contributing to our project. All issue types follow the same general lifecycle. Differences are noted
below.

1. Issue creation
2. Triage
    - The maintainer in charge of triaging will apply the proper labels for the issue. This includes
      labels for type and status (such as `bug`).
    - (If needed) Clean up the title to succinctly and clearly state the issue.
3. Discussion
    - Issues that are labeled as `feature` or `bug` should be connected to the PR that resolves it.
    - Whoever is working on a `feature` or `bug` issue (whether a maintainer or someone from the
      community), should either assign the issue to themself or make a comment in the issue saying
      that they are taking it.
    - `feature` and `question` issues should stay open until resolved or if they have not
      been active for more than 90 days. This will help keep the issue queue to a manageable size
      and reduce noise. Should the issue need to stay open, the `keep open` label can be added.
4. Issue closure

### Contributing code
To make it easy for you to contribute code, please consider the following steps to make a clean pull
 request:

- Create a personal fork of the project on Github.
- Clone the fork on your local machine. Your remote repo on Github is called `origin`.
- Add the original repository as a remote called `upstream`.
- If you created your fork a while ago be sure to pull upstream changes into your local repository.
- Create a new personal branch to work on! Base this branch on the `master` branch and give it at meaningful name, e
.g. `adding_Java9_support`.
- Implement/fix your feature, comment your code.
- If the project has tests run them!
- Write or adapt tests as needed.
- Squash your commits into a single commit, if you think it's necessary.
- Push your branch to your fork on Github, the remote `origin`.
- From your fork open a pull request in the `master` branch
- Write your commit messages in the present tense. Your commit message should describe what the commit, when applied
  , does to the code – not what you did to the code.
- Once the pull request is approved and merged you can pull the changes from `upstream` to your local repo and delete
your extra branch(es).

To smoothly integrate your code, please follow the [Code Conventions](#Code-Conventions-and-Housekeeping) of
 the project, including indentation and code style. Thank you!

The description is adapted from the brilliant description that can be found 
[here](https://github.com/MarcDiethelm/contributing/blob/master/README.md).

### Branching workflow
To keep the branching workflow simple, we follow the 
[GitHub flow](https://guides.github.com/introduction/flow/) workflow. The most important aspect is that the `master`
 branch is always deployable. For managing releases, we're adding corresponding tags on the `master` branch. To
  make the version numbers of our releases easy to understand, we follow the well-known 
  [Semver](https://semver.org/) specification.

## Need support?
_Ref to support address_

Or open an issue?
