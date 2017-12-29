# ScalaTest-Training
Training with org.scalatest, org.scalamock and org.scalacheck

## Recommendations
Use IntelliJ IDEA
* Version: 2017.3.2
  Build: 173.4127.27
  Released: December 26, 2017
  
Use JRE 1.8.0
Use Simple Build Tool (SBT)

## What it Gives You

* SuiteTest1: Initial example of suite 
* SuiteTest2: All testing styles 
* SuiteTest3: Working with asserts 
* SuiteTest4: TDD Exercise 
* SuiteTest5: Tagging tests 
* SuiteTest6: Sharing fixtures: fixture-methods and before/after methods 
* SuiteTest7: Sharing tests
* SuiteTest8: Matchers
* SuiteTest9: Acceptance Tests Exercise
* SuiteTest10: ScalaMock object
* SuiteTest11: Property-based testing - ScalaCheck


## Basic Use

* Download project
* Import project to IntelliJ
* Execute sbt shell in terminal or within IntelliJ:

```
sbt
```
* Build proyect. Execute compile on sbt

```
compile
```
* Test suites. Execute test or testOnly on sbt

```
test
testOnly *.*Suite
```
