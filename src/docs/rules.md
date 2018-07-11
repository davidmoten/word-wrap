## Wrapping rules
The rules below are generated from the unit tests in `WordWrapTest.java`. The default wrapping configuration used is a line length of 6 characters. The $ symbol is used to represent a new line character.

1. **breakOnCommaDoesNotHappenWithoutSpaceAfter**
Input:
```
hi,there
```
Output:
```
hi,th-$
ere
```

2. **breakOnCommasWithDigits**
Input:
```
1,2,3,4,5,6,7,8,9
```
Output:
```
1,2,3,$
4,5,6,$
7,8,9
```

3. **breakOnQuote**
Input:
```
says␣'helo'
```
Output:
```
says$
'helo'
```

4. **breakQuoteInMiddle**
Input:
```
why␣he's␣nasty
```
Output:
```
why$
he's$
nasty
```

5. **carriageReturnRemoved**
Input:
```
hello$
there
```
Output:
```
hello$
there
```

6. **conserveWhitespace**
Input:
```
␣␣ab$
␣␣␣cd$
␣␣ef$
$
hi
```
Output:
```
␣␣ab$
␣␣␣cd$
␣␣ef$
$
hi
```

7. **dontBreakOnQuestionMark**
Input:
```
ab␣cde?
```
Output:
```
ab$
cde?
```

8. **emptyText**
Input:
```

```
Output:
```

```

9. **endWithNewLine**
Input:
```
a$

```
Output:
```
a$

```

10. **longLineALotOfWhiteSpaceInMiddle**
Input:
```
hello␣␣␣␣␣␣␣␣␣␣there
```
Output:
```
hello$
there
```

11. **longLineSplitsOnWhiteSpace**
Input:
```
hello␣there
```
Output:
```
hello$
there
```

12. **longLineWithTwoSpacesInMiddle**
Input:
```
hello␣␣there
```
Output:
```
hello$
there
```

13. **longThenShort**
Input:
```
hellothere$
␣␣boo
```
Output:
```
hello-$
there$
␣␣boo
```

14. **longThenShortWithMoreLines**
Input:
```
hellothere$
␣␣boo$
␣␣hi
```
Output:
```
hello-$
there$
␣␣boo$
␣␣hi
```

15. **longWhitespaceLastLine**
Input:
```
␣␣␣␣␣␣␣␣␣␣
```
Output:
```

```

16. **longWhitespaceThenNewLine**
Input:
```
␣␣␣␣␣␣␣␣␣␣$

```
Output:
```
$

```

17. **longWhitespaceThenWord**
Input:
```
␣␣␣␣␣␣␣␣a
```
Output:
```
$
a
```

18. **longWordForcesBreak**
Input:
```
hellothere
```
Output:
```
hello-$
there
```

19. **newLineCharacterPreserved**
Input:
```
hello$
there
```
Output:
```
hello$
there
```

20. **noHyphenAfterDigits**
Input:
```
1234567890
```
Output:
```
123456$
7890
```

21. **oneLetter**
Input:
```
a
```
Output:
```
a
```

22. **precedingWhitespaceConserved**
Input:
```
␣␣he
```
Output:
```
␣␣he
```

23. **precedingWhitespaceLongWord**
Input:
```
␣␣helloyou
```
Output:
```
␣␣hel-$
loyou
```

24. **shortLineHasWhitespace**
Input:
```
hi␣bo
```
Output:
```
hi␣bo
```

25. **shortLineNoWhitespace**
Input:
```
hello
```
Output:
```
hello
```

26. **shortThenLong**
Input:
```
hi␣mygoodnessme
```
Output:
```
hi$
mygoo-$
dness-$
me
```

27. **spaceAndQuestionMark**
Input:
```
␣␣?
```
Output:
```
␣␣?
```

28. **spaceThenOneLetter**
Input:
```
␣a
```
Output:
```
␣a
```

29. **whitespaceConservedAfterNewLine**
Input:
```
hello$
␣there
```
Output:
```
hello$
␣there
```

30. **whitespacePreservedAfterNewLine**
Input:
```
hello$
␣␣the
```
Output:
```
hello$
␣␣the
```

31. **wrapRightTrimsWhitespaceBeforeNewLine**
Input:
```
abc␣␣␣␣$
cde␣␣␣
```
Output:
```
abc$
cde␣␣␣
```

