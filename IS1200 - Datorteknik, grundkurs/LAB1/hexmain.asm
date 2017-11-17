  # hexmain.asm
  # Written 2015-09-04 by F Lundevall
  # Copyright abandonded - this file is in the public domain.
  
  # hecasc by C Johansson

	.text
main:
	li	$a0,9		# change this to test different values

	jal	hexasc		# call hexasc
	nop			# delay slot filler (just in case)	

	move	$a0,$v0		# copy return value to argument register

	li	$v0,11		# syscall with v0 = 11 will print out
	syscall			# one byte from a0 to the Run I/O window
	
stop:	j	stop		# stop after one run
	nop			# delay slot filler (just in case)

  # You can write your own code for hexasc here
  #
hexasc:
	andi	$a0, $a0, 0x0f	# Kopierar dem 4 minsta bitarna, alla andra ignoreras
	move	$t0, $a0	# Sparar den i t0
	
	li	$t3, 0x09	# t3 s�tts som 9, f�r att kunna kolla om det �r en siffra eller bokstav senare
	slt	$t1, $t3, $a0	# Kontrollerar om talet �r st�rre �n 9, st�mmer det, t1 == 1
	mul	$t2, $t1, 0x07	# t2 blir t1 g�nger 7, allts� blir det 0 eller 7
	add	$t0, $t0, $t2	# Om talet �r en bokstav s� kommer 7 adderas f�r att allt ska bli r�tt i tabellen 
	
	addi	$t0, $t0, 0x30	# F�rflyttar hela 30 steg
	move	$v0, $t0	# F�rbereder return genom att spara i v0
	
	jr	$ra		# Returnerar
	nop			# delay slot filler (just in case)
