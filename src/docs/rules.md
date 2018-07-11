## Wrapping rules
The rules below are generated from the unit tests in `WordWrapTest.java`. The default wrapping configuration used is a line length of 6 characters.

1. **breakOnCommaDoesNotHappenWithoutSpaceAfter**
```
hi,there
```
:arrow_down:
```
hi,th-$
ere
```

2. **breakOnCommasWithDigits**
```
1,2,3,4,5,6,7,8,9
```
:arrow_down:
```
1,2,3,$
4,5,6,$
7,8,9
```

3. **breakOnQuote**
```
says␣'helo'
```
:arrow_down:
```
says$
'helo'
```

4. **breakQuoteInMiddle**
```
why␣he's␣nasty
```
:arrow_down:
```
why$
he's$
nasty
```

5. **carriageReturnRemoved**
```
hello$
there
```
:arrow_down:
```
hello$
there
```

6. **conserveWhitespace**
```
␣␣ab$
␣␣␣cd$
␣␣ef$
$
hi
```
:arrow_down:
```
␣␣ab$
␣␣␣cd$
␣␣ef$
$
hi
```

7. **dontBreakOnQuestionMark**
```
ab␣cde?
```
:arrow_down:
```
ab$
cde?
```

8. **emptyText**
```

```
:arrow_down:
```

```

9. **endWithNewLine**
```
a$

```
:arrow_down:
```
a$

```

10. **longLineALotOfWhiteSpaceInMiddle**
```
hello␣␣␣␣␣␣␣␣␣␣there
```
:arrow_down:
```
hello$
there
```

11. **longLineSplitsOnWhiteSpace**
```
hello␣there
```
:arrow_down:
```
hello$
there
```

12. **longLineWithTwoSpacesInMiddle**
```
hello␣␣there
```
:arrow_down:
```
hello$
there
```

13. **longThenShort**
```
hellothere$
␣␣boo
```
:arrow_down:
```
hello-$
there$
␣␣boo
```

14. **longThenShortWithMoreLines**
```
hellothere$
␣␣boo$
␣␣hi
```
:arrow_down:
```
hello-$
there$
␣␣boo$
␣␣hi
```

15. **longWhitespaceLastLine**
```
␣␣␣␣␣␣␣␣␣␣
```
:arrow_down:
```

```

16. **longWhitespaceThenNewLine**
```
␣␣␣␣␣␣␣␣␣␣$

```
:arrow_down:
```
$

```

17. **longWhitespaceThenWord**
```
␣␣␣␣␣␣␣␣a
```
:arrow_down:
```
$
a
```

18. **longWordForcesBreak**
```
hellothere
```
:arrow_down:
```
hello-$
there
```

19. **newLineCharacterPreserved**
```
hello$
there
```
:arrow_down:
```
hello$
there
```

20. **noHyphenAfterDigits**
```
1234567890
```
:arrow_down:
```
123456$
7890
```

21. **oneLetter**
```
a
```
:arrow_down:
```
a
```

22. **precedingWhitespaceConserved**
```
␣␣he
```
:arrow_down:
```
␣␣he
```

23. **precedingWhitespaceLongWord**
```
␣␣helloyou
```
:arrow_down:
```
␣␣hel-$
loyou
```

24. **shortLineHasWhitespace**
```
hi␣bo
```
:arrow_down:
```
hi␣bo
```

25. **shortLineNoWhitespace**
```
hello
```
:arrow_down:
```
hello
```

26. **shortThenLong**
```
hi␣mygoodnessme
```
:arrow_down:
```
hi$
mygoo-$
dness-$
me
```

27. **spaceAndQuestionMark**
```
␣␣?
```
:arrow_down:
```
␣␣?
```

28. **spaceThenOneLetter**
```
␣a
```
:arrow_down:
```
␣a
```

29. **whitespaceConservedAfterNewLine**
```
hello$
␣there
```
:arrow_down:
```
hello$
␣there
```

30. **whitespacePreservedAfterNewLine**
```
hello$
␣␣the
```
:arrow_down:
```
hello$
␣␣the
```

31. **wrapRightTrimsWhitespaceBeforeNewLine**
```
abc␣␣␣␣$
cde␣␣␣
```
:arrow_down:
```
abc$
cde␣␣␣
```

