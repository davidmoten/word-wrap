1. breakOnCommaDoesNotHappenWithoutSpaceAfter
```
hi,there
```
 ->
```
hi,th-$
ere
```

2. breakOnCommasWithDigits
```
1,2,3,4,5,6,7,8,9
```
 ->
```
1,2,3,$
4,5,6,$
7,8,9
```

3. breakOnQuote
```
says␣'helo'
```
 ->
```
says$
'helo'
```

4. breakQuoteInMiddle
```
why␣he's␣nasty
```
 ->
```
why$
he's$
nasty
```

5. carriageReturnRemoved
```
hello$
there
```
 ->
```
hello$
there
```

6. conserveWhitespace
```
␣␣ab$
␣␣␣cd$
␣␣ef$
$
hi
```
 ->
```
␣␣ab$
␣␣␣cd$
␣␣ef$
$
hi
```

7. dontBreakOnQuestionMark
```
ab␣cde?
```
 ->
```
ab$
cde?
```

8. emptyText
```

```
 ->
```

```

9. endWithNewLine
```
a$

```
 ->
```
a$

```

10. longLineALotOfWhiteSpaceInMiddle
```
hello␣␣␣␣␣␣␣␣␣␣there
```
 ->
```
hello$
there
```

11. longLineSplitsOnWhiteSpace
```
hello␣there
```
 ->
```
hello$
there
```

12. longLineWithTwoSpacesInMiddle
```
hello␣␣there
```
 ->
```
hello$
there
```

13. longThenShort
```
hellothere$
␣␣boo
```
 ->
```
hello-$
there$
␣␣boo
```

14. longThenShortWithMoreLines
```
hellothere$
␣␣boo$
␣␣hi
```
 ->
```
hello-$
there$
␣␣boo$
␣␣hi
```

15. longWhitespaceLastLine
```
␣␣␣␣␣␣␣␣␣␣
```
 ->
```

```

16. longWhitespaceThenNewLine
```
␣␣␣␣␣␣␣␣␣␣$

```
 ->
```
$

```

17. longWhitespaceThenWord
```
␣␣␣␣␣␣␣␣a
```
 ->
```
$
a
```

18. longWordForcesBreak
```
hellothere
```
 ->
```
hello-$
there
```

19. newLineCharacterPreserved
```
hello$
there
```
 ->
```
hello$
there
```

20. noHyphenAfterDigits
```
1234567890
```
 ->
```
123456$
7890
```

21. oneLetter
```
a
```
 ->
```
a
```

22. precedingWhitespaceConserved
```
␣␣he
```
 ->
```
␣␣he
```

23. precedingWhitespaceLongWord
```
␣␣helloyou
```
 ->
```
␣␣hel-$
loyou
```

24. shortLineHasWhitespace
```
hi␣bo
```
 ->
```
hi␣bo
```

25. shortLineNoWhitespace
```
hello
```
 ->
```
hello
```

26. shortThenLong
```
hi␣mygoodnessme
```
 ->
```
hi$
mygoo-$
dness-$
me
```

27. spaceAndQuestionMark
```
␣␣?
```
 ->
```
␣␣?
```

28. spaceThenOneLetter
```
␣a
```
 ->
```
␣a
```

29. whitespaceConservedAfterNewLine
```
hello$
␣there
```
 ->
```
hello$
␣there
```

30. whitespacePreservedAfterNewLine
```
hello$
␣␣the
```
 ->
```
hello$
␣␣the
```

31. wrapRightTrimsWhitespaceBeforeNewLine
```
abc␣␣␣␣$
cde␣␣␣
```
 ->
```
abc$
cde␣␣␣
```

