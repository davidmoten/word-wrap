## Wrapping rules
The rules below are generated from the unit tests in `WordWrapTest.java`. The default wrapping configuration used is a line length of 6 characters.

1. **breakOnCommaDoesNotHappenWithoutSpaceAfter**
```
hi,there
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
hi,th-$
ere
```

2. **breakOnCommasWithDigits**
```
1,2,3,4,5,6,7,8,9
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
1,2,3,$
4,5,6,$
7,8,9
```

3. **breakOnQuote**
```
says␣'helo'
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
says$
'helo'
```

4. **breakQuoteInMiddle**
```
why␣he's␣nasty
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
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
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
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
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
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
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
ab$
cde?
```

8. **emptyText**
```

```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```

```

9. **endWithNewLine**
```
a$

```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
a$

```

10. **longLineALotOfWhiteSpaceInMiddle**
```
hello␣␣␣␣␣␣␣␣␣␣there
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
hello$
there
```

11. **longLineSplitsOnWhiteSpace**
```
hello␣there
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
hello$
there
```

12. **longLineWithTwoSpacesInMiddle**
```
hello␣␣there
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
hello$
there
```

13. **longThenShort**
```
hellothere$
␣␣boo
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
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
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
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
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```

```

16. **longWhitespaceThenNewLine**
```
␣␣␣␣␣␣␣␣␣␣$

```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
$

```

17. **longWhitespaceThenWord**
```
␣␣␣␣␣␣␣␣a
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
$
a
```

18. **longWordForcesBreak**
```
hellothere
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
hello-$
there
```

19. **newLineCharacterPreserved**
```
hello$
there
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
hello$
there
```

20. **noHyphenAfterDigits**
```
1234567890
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
123456$
7890
```

21. **oneLetter**
```
a
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
a
```

22. **precedingWhitespaceConserved**
```
␣␣he
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
␣␣he
```

23. **precedingWhitespaceLongWord**
```
␣␣helloyou
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
␣␣hel-$
loyou
```

24. **shortLineHasWhitespace**
```
hi␣bo
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
hi␣bo
```

25. **shortLineNoWhitespace**
```
hello
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
hello
```

26. **shortThenLong**
```
hi␣mygoodnessme
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
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
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
␣␣?
```

28. **spaceThenOneLetter**
```
␣a
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
␣a
```

29. **whitespaceConservedAfterNewLine**
```
hello$
␣there
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
hello$
␣there
```

30. **whitespacePreservedAfterNewLine**
```
hello$
␣␣the
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
hello$
␣␣the
```

31. **wrapRightTrimsWhitespaceBeforeNewLine**
```
abc␣␣␣␣$
cde␣␣␣
```
&nbsp;&nbsp;&nbsp;&nbsp;:arrow_down:
```
abc$
cde␣␣␣
```

