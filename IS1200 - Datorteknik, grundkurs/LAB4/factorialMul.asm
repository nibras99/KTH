addi	$a0, $0, 0	# Factorial
addi	$v0, $0, 1	# Svaret �r standard som 1, vilket det blir vid 0!
beq	$a0, $0, done
addi	$a1, $0, 0	# V�rat i, b�rjar p� 0 eftersom vi �kar det direkt i b�rjan

outerLoop:
addi	$a1, $a1, 1	# �kar a1 (i) med 1
mul	$v0, $v0, $a1	# v0 = v0 g�nger a1
beq	$a1, $a0, done	# Om v�rat i �r lika med a0 s� har vi g�tt igenom allt
beq	$0, $0, outerLoop

done:
beq	$0, $0, done	# Klar, fastnar i denna loop f�r evigt