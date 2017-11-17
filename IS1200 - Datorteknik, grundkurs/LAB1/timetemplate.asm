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
timstr:	.ascii "text text lost of text"
delaytime: .word 1000	# Tiden för delay 4711, hur lång tid en ms tar i detta program / chip / processor
	.text

main:
	# print timstr
	la	$a0,timstr
	li	$v0,4
	syscall
	nop
	# wait a little
	li	$a0, 1000 # Hur många ms's delay det skall vara
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

  # Kod nedan skriven av ME
  #
hexasc:
	andi	$a0, $a0, 0x0f	# Kopierar dem 4 minsta bitarna, alla andra ignoreras
	move	$t0, $a0	# Sparar den i t0
	
	li	$t3, 0x09	# t3 sätts som 9, för att kunna kolla om det är en siffra eller bokstav senare
	slt	$t1, $t3, $a0	# Kontrollerar om talet är större än 9, stämmer det, t1 == 1
	mul	$t2, $t1, 0x07	# t2 blir t1 gånger 7, alltså blir det 0 eller 7
	add	$t0, $t0, $t2	# Om talet är en bokstav så kommer 7 adderas för att allt ska bli rätt i tabellen 
	
	addi	$t0, $t0, 0x30	# Förflyttar hela 30 steg
	move	$v0, $t0	# Förbereder return genom att spara i v0
	
	jr	$ra		# Returnerar
	nop			# delay slot filler (just in case)

delay:
	blez	$a0, skip	# om inte denna finns så fungerar klockan utan delay, annars fastnar den i loopen
	nop
	
	addi 	$t1, $0, 0	# i = 0
	add	$t4, $a0, $0	# antal ms den skall köras
	lw 	$t2, delaytime
	
	while:
		loop:
		slt	$t0, $t1, $t2 #om t1 är mindre än t2, t0 = 1
		addi	$t1, $t1, 1
		bnez	$t0, loop
		nop
		
	addi	$t4, $t4, -1
	bnez	$t4, while
	nop
	
	skip:
	jr $ra
	nop
			
time2string:
	move	$t9, $a0		# "kopierar" över adressen till mytime
	PUSH	($ra) 			# Sparar så att vi inte skriver över
	PUSH	($s1) 			# ^^^
	PUSH	($s2) 			# ^^
	PUSH	($s3) 			# ^^
	PUSH	($s4)
	move	$s1, $a1		# Kopierar över värdet i adressen till s1
	
	addi	$s4, $0, 0
	# Minutplatsen till vänster    0000 0000 0000 0101 
	andi	$a0, $s1, 0xf000	# Genom andi så gör vi allt utom de fyra bitarna vi vill behålla till nollor.
	srl	$a0, $a0, 12		# Förflyttar dem fyra bitarna längst till höger, alltså mot LSB
	jal	hexasc			# Konverterar till ASCII
	nop				# v0 = 0x35 = 0000 0000 0011 0101
	sb	$v0, 0($t9)
	
	add	$s4, $s4, $v0		# # <<<<< Dessa två rader, använder s4, för att spara talet i ASCII utan :
	sll	$s4, $s4, 8
	
	# Minutplatsen till höger
	andi	$a0, $s1, 0x0f00	# Genom andi så gör vi allt utom de fyra bitarna vi vill behålla till nollor.
	srl	$a0, $a0, 8		# Förflyttar dem fyra bitarna längst till höger, alltså mot LSB
	jal	hexasc			# Konverterar till ASCII
	nop
	sb	$v0, 1($t9)
	
	add	$s4, $s4, $v0		# <<<<<
	sll	$s4, $s4, 8
	
	# Kolon
	li	$t3, 0x3a		# ASCII koden för kolon
	sb	$t3, 2($t9)		# Lägger in ":" i minnesplats 3, t9 = minnesadressen
	
	# Sekundplatsen till vänster
	andi	$a0, $s1, 0x00f0	# samesame
	srl	$a0, $a0, 4		# samesame
	jal	hexasc			# samesame
	nop
	sb	$v0, 3($t9)
	
	add	$s4, $s4, $v0		# <<<<<
	sll	$s4, $s4, 8
	
	# Sekundplatsen till höger
	andi	$a0, $s1, 0x000f	# Hämtar sista biten
	jal	hexasc			# Behöver inte förflytta, så konvertera direkt.
	nop
	sb	$v0, 4($t9)
	
	add	$s4, $s4, $v0		# <<<<<
	
	li	$t4, 0x00		# 0x00 = null
	sb	$t4, 5($t9)
	
	addi	$t5, $0, 0x30303030
	beq	$t5, $s4, zero
	
	zerodone:
	
	POP	($s4)
	POP	($s3)			# ^^^
	POP	($s2)			# Laddar tillbaka från minnet
	POP	($s1)			# ^^^
	POP	($ra)			# ^^
	jr	$ra
	nop
	
	zero:
	addi	$t0, $0, 0x52554F48
	addi	$t1, $0, 0x00
	sw	$t0, 0($t9)
	sb	$t1, 4($t9)
	j	zerodone
	nop
		
	
