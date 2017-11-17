addi	$a0, $0, 0	# Factorial
addi	$v0, $0, 1	# Svaret är standard som 1, vilket det blir vid 0!
beq		$a0, $0, done
addi	$a1, $0, 0	# Vårat i, börjar på 0 eftersom vi ökar det direkt i början

outerLoop:
addi	$a1, $a1, 1	# Ökar a1 (i) med 1
beq	$a1, $a0, done	# Om vårat i är lika med a0 så har vi gått igenom allt
add	$a2, $0, $v0	# Ökar a2 med v0
add	$a3, $0, $0	# Säter a3 som 0, vårat inre "i" (j)

innerLoop:
beq		$a3, $a1, outerLoop	# Om a3 är lika med a1, så har vi uppnåt den inre "for" loopens villkor
add		$v0, $v0, $a2		# Sätter v0 som v0 + a2, alltså vad v0 är nu + 1
addi	$a3, $a3, 1		# Ökar a3 med ett, vårat inre "i"
beq		$0, $0, innerLoop	# Om 0 är lika med 0 så fortsätter loopen....

done:
beq		$0, $0, done		# Klar, fastnar i denna loop för evigt