addi	$a0, $0, 0	# Factorial
addi	$v0, $0, 1	# Svaret är standard som 1, vilket det blir vid 0!
beq	$a0, $0, done
addi	$a1, $0, 0	# Vårat i, börjar på 0 eftersom vi ökar det direkt i början

outerLoop:
addi	$a1, $a1, 1	# Ökar a1 (i) med 1
mul	$v0, $v0, $a1	# v0 = v0 gånger a1
beq	$a1, $a0, done	# Om vårat i är lika med a0 så har vi gått igenom allt
beq	$0, $0, outerLoop

done:
beq	$0, $0, done	# Klar, fastnar i denna loop för evigt