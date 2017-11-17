  # timetemplate.asm
  # Written 2015 by F Lundevall
  # Copyright abandonded - this file is in the public domain.

.macro	PUSH (%reg)
	addi	$sp,$sp,-4
	sw	%reg,0($sp)
.end_macro

.macro	POP (%reg)
	lw	%reg,0($sp)
	addi	$sp,$sp,4
.end_macro

	.data
	.align 2
mytime:	.word 0x5957
timstr:	.ascii "text more text lots of text\0"
delaytime: .word 4711	# Time needed for a delay. Change for better clock sync

	.text
main:
	# print timstr
	la	$a0,timstr
	li	$v0,4
	syscall
	nop
	# wait a little
	li	$a0,2
	jal	delay
	nop
	# call tick
	la	$a0,mytime
	jal	tick
	nop
	# call your function time2string
	la	$a0,timstr
	la	$t0,mytime
	lw	$a1,0($t0)
	jal	time2string
	nop
	# print a newline
	li	$a0,10
	li	$v0,11
	syscall
	nop
	# go back and do it all again
	j	main
	nop
# tick: update time pointed to by $a0
tick:	lw	$t0,0($a0)	# get time
	addiu	$t0,$t0,1	# increase
	andi	$t1,$t0,0xf	# check lowest digit
	sltiu	$t2,$t1,0xa	# if digit < a, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0x6	# adjust lowest digit
	andi	$t1,$t0,0xf0	# check next digit
	sltiu	$t2,$t1,0x60	# if digit < 6, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0xa0	# adjust digit
	andi	$t1,$t0,0xf00	# check minute digit
	sltiu	$t2,$t1,0xa00	# if digit < a, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0x600	# adjust digit
	andi	$t1,$t0,0xf000	# check last digit
	sltiu	$t2,$t1,0x6000	# if digit < 6, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0xa000	# adjust last digit
tiend:	sw	$t0,0($a0)	# save updated result
	jr	$ra		# return
	nop

  # you can write your code for subroutine "hexasc" below this line
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

delay:
	lw	$t0, delaytime		# Get loops to wait for one ms
	mul	$t0, $t0, $a0		# Get total amount of loops to wait
	 
	delaystart:			# Start the loop	
	sub	$t0, $t0, 1		# Subtract one
	slti	$t1, $t0, 0		# Find if smaller than 0
	bne	$t1 1, delaystart	# End the loop

	jr	$ra
	nop
	
time2string:
	move	$t9, $a0
	PUSH	($ra) 			# Save for later
	PUSH	($s1) 			# Save for later
	PUSH	($s2) 			# Save for later
	move	$s1, $a1

	# First minute digit
	andi	$a0, $s1, 0xf000	# Get the first minute digit
	srl	$a0, $a0, 12		# Get the nibble
	jal	hexasc			# Convert nibble to ASCII
	nop
	move	$s2, $v0		# Create time string
	
	# Second minute digit
	andi	$a0, $s1, 0x0f00	# Get the second minute digit
	srl	$a0, $a0, 8		# Get the nibble
	jal	hexasc			# Convert nibble to ASCII
	nop
	sll	$v0, $v0, 8		# Position character before concatenation
	add	$s2, $s2, $v0		# Concatenate time string
	
	# Colon
	li	$t2, 0x3a		# Load ASCII code for colon
	sll	$t2, $t2, 16		# Position the colon
	add	$s2, $s2, $t2		# Concatenate time string
	
	# First second digit
	andi	$a0, $s1, 0x00f0	# Get the first second digit
	srl	$a0, $a0, 4		# Get the nibble
	jal	hexasc			# Convert nibble to ASCII
	nop
	sll	$v0, $v0, 24		# Position the character
	add	$s2, $s2, $v0		# Concatenate time string
	
	# Store first 4 ASCII characters
	sw	$s2, 0($t9)		# Store to memory
	
	# Second second digit
	andi	$a0, $s1, 0x000f	# Get the first second digit
	jal	hexasc			# Convert nibble to ASCII
	nop
	sw	$v0, 4($t9)		# Store last digit

	POP	($s2)			# Load back from memory
	POP	($s1)			# Load back from memory
	POP	($ra)			# Load back from memory
	jr	$ra
	nop	