  # labwork.S
  # Written 2015 by F Lundevall
  # Skeleton file for IS1200/IS1500 lab 1 2015-2016.
  # The macros PUSH and POP are in the public domain.
  
.macro	PUSH reg
	addi	$sp,$sp,-4
	sw	\reg,0($sp)
.endm

.macro	POP reg
	lw	\reg,0($sp)
	addi	$sp,$sp,4
.endm

  # You can add your own code here CJ NEDAN
  # . /opt/mcb32tools/environment
  # make install TTYDEV=/dev/ttyS2
.data #CJ

delaytime: .word 20000000	# Tiden för delay 4711

.text
.global hexasc, delay, time2string

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
		slt	$t0, $t1, $t2 
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
	PUSH	$ra 			# Sparar så att vi inte skriver över
	PUSH	$s1				# ^^^
	PUSH	$s2 			# ^^
	PUSH	$s3 			# ^^
	move	$s1, $a1		# Kopierar över värdet i adressen till s1

	# Minutplatsen länst till vänster    0000 0000 0000 0101 
	andi	$a0, $s1, 0xf000	# Genom andi så gör vi allt utom de fyra bitarna vi vill behålla till nollor.
	srl	$a0, $a0, 12		# Förflyttar dem fyra bitarna längst till höger, alltså mot LSB
	jal	hexasc			# Konverterar till ASCII
	nop				# v0 = 0x35 = 0000 0000 0011 0101
	sb	$v0, 0($t9)
	
	# Minutplatsen till vänster
	andi	$a0, $s1, 0x0f00	# Genom andi så gör vi allt utom de fyra bitarna vi vill behålla till nollor.
	srl	$a0, $a0, 8		# Förflyttar dem fyra bitarna längst till höger, alltså mot LSB
	jal	hexasc			# Konverterar till ASCII
	nop
	sb	$v0, 1($t9)
	
	# Kolon
	li	$t3, 0x3a		# ASCII koden för kolon
	sb	$t3, 2($t9)
	
	# Minutplatsen tillvänster höger
	andi	$a0, $s1, 0x00f0	# samesame
	srl	$a0, $a0, 4		# samesame
	jal	hexasc			# samesame
	nop
	sb	$v0, 3($t9)
	
	# Sekundplatsen till höger
	andi	$a0, $s1, 0x000f	# Hämtar sista biten
	jal	hexasc			# Behöver inte förflytta, så konvertera direkt.
	nop
	sb	$v0, 4($t9)
	
	li	$t4, 0x00
	sb	$t4, 5($t9)
	
	POP	$s3			# ^^^
	POP	$s2			# Laddar tillbaka från minnet
	POP	$s1			# ^^^
	POP	$ra			# ^^
	jr	$ra
	nop