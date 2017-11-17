addi	$a0, $0, 0	# Factorial
addi	$v0, $0, 1	# Svaret �r standard som 1, vilket det blir vid 0!
beq		$a0, $0, done
addi	$a1, $0, 0	# V�rat i, b�rjar p� 0 eftersom vi �kar det direkt i b�rjan

outerLoop:
addi	$a1, $a1, 1	# �kar a1 (i) med 1
beq	$a1, $a0, done	# Om v�rat i �r lika med a0 s� har vi g�tt igenom allt
add	$a2, $0, $v0	# �kar a2 med v0
add	$a3, $0, $0	# S�ter a3 som 0, v�rat inre "i" (j)

innerLoop:
beq		$a3, $a1, outerLoop	# Om a3 �r lika med a1, s� har vi uppn�t den inre "for" loopens villkor
add		$v0, $v0, $a2		# S�tter v0 som v0 + a2, allts� vad v0 �r nu + 1
addi	$a3, $a3, 1		# �kar a3 med ett, v�rat inre "i"
beq		$0, $0, innerLoop	# Om 0 �r lika med 0 s� forts�tter loopen....

done:
beq		$0, $0, done		# Klar, fastnar i denna loop f�r evigt