
        # pointers.asm
# By David Broman, 2015-09-14
# This file is in the public domain


.macro	PUSH (%reg)
	addi	$sp,$sp,-4
	sw	%reg,0($sp)
.end_macro

.macro	POP (%reg)
	lw	%reg,0($sp)
	addi	$sp,$sp,4
.end_macro

.data


text1: 	  .asciiz "This is a string." # En string med 17 platser
text2:	  .asciiz "Yet another thing." # En string med 18 platser

.align 2
list1: 	.space 80	# Skapar utrymme f�r 80 byte, allts� plats f�r 80 chars i C, 1 byte = 1 char plats.
list2: 	.space 80	# Skapar utrymme f�r 80 byte till
count:	.word  0	# Skapar ett word ned inneh�lelt 0

.text
main:
	jal	work	# Hoppar till work, sparar returadressen
stop:	j	stop	# Hoppar till stop, "fastnar h�r".

# function work()
work:
	PUSH	($ra)		# Pushar och sparar
	la 	$a0,text1	# L�gger adressen till stringen text1 i a0
	la	$a1,list1	# L�gger adressen till det sparade utrymmet list1 i a1
	la	$a2,count	# L�gger adressen till det sparade wordet count i a2
	jal	copycodes	# Hoppar till copycodes, sparar returv�rdet.
	
	la 	$a0,text2	# L�gger adressen till stringen text2 i a0
	la	$a1,list2	# L�gger adressen till det sparade utrymmet list2 i a1
	la	$a2,count	# L�gger adressen till det sparade wordet count i a2
	jal	copycodes	# Hoppar till copycodes, sparar returv�rdet.
	POP	($ra)		# Allt �r klart, poppar och f�r tillbaka v�rdet (returadressen)
	
	
# function copycodes()
copycodes:
loop:
	lb	$t0,0($a0)	# Laddar byten i plats 0 i a0 till t0, i f�rsta fallet = 0x54
	beq	$t0,$0,done	# Om t0 �r lika med 0, allts� 0x00, allts� NULL, s� �r programmet klart och hoppar till done
	sw	$t0,0($a1)	# Lagrar ordet i t0 till rad 0 / f�rsta byten i a1, allts� i fall 1 s� lagrar vi T till f�rsta platsen i list1

	addi	$a0,$a0,1	# �kar a0 med 1, allts� g�r till n�sta adressplats / byte plats.
	addi	$a1,$a1,4	# �kar a1 med 4, allts� g�r till n�sta minnesrad (0, 4, 8, 12....)
	
	lw	$t1,0($a2)	# Laddar f�rsta raden ur a2, allts� ur count, allts� f�rsta 4 bytesen ( av 32.)
	addi	$t1,$t1,1	# Adderar ett till t1
	sw	$t1,0($a2)	# Sparar tillbaka det i count
	
	j	loop		# Forts�tt tills rad 2 uppfylls, allts� tills vi n�tt null.
done:
	jr	$ra		# Vi �r nu klar, returnerar och kommer att fastna i stop efter main loopen.
		



