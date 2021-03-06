
	Type tItem
		Field ID
		Field Type1
		Field Type2
		Field Type3
		Field IsHeld
	End Type
	Global TOTALITEMS

	Function Item_Create.tItem(id#, type1#, type2#, type3#, isheld=false)
		ii.tItem = New tItem
		TOTALITEMS=TOTALITEMS+1
		ii\ID=id#
		ii\Type1=type1#
		ii\Type2=type2#
		ii\Type3=type3#
		ii\IsHeld=isheld
		Return ii
	End Function

;--------------------------------------------------------------------------------------------------------------------------

	Type tCarriedItem
		Field ID
		Field Type1
		Field Type2
	End Type
	Global TOTALCARRIEDITEMS

	Function CarriedItem_Create.tCarriedItem(id#, type1#, type2#)
		cii.tCarriedItem = New tCarriedItem
		TOTALCARRIEDITEMS=TOTALCARRIEDITEMS+1
		cii\ID=id#
		cii\Type1=type1#
		cii\Type2=type2#
		Return cii
	End Function

	Function CarriedItem_CreateFromTouch(type1#, type2#, limit=30)
		If TOTALCARRIEDITEMS>=limit Then
			For cii.tCarriedItem = Each tCarriedItem
			If cii\ID=limit Then Delete cii
			Next
		EndIf
		For cii.tCarriedItem = Each tCarriedItem : cii\ID=cii\ID+1 : Next
		cii2.tCarriedItem = CarriedItem_Create(1, type1#, type2#)
	End Function

;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------

Function GetShallExplodeInventory()
	foundsomething=false
	For cii.tCarriedItem=Each tCarriedItem
		foundsomething=true
	Next
	For ii.tItem=Each tItem
		If ii\IsHeld Then foundsomething=true
	Next
	Game\Interface\ShallExplodeInventory=foundsomething
End Function

;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------

	Dim ITEM_MAX(6)
	ITEM_MAX(1)=51
	ITEM_MAX(2)=64
	ITEM_MAX(3)=14
	ITEM_MAX(4)=2
	ITEM_MAX(5)=13
	ITEM_MAX(6)=1

;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------

	Dim ITEM_AVAILABLE_FRUITS(ITEM_MAX(1))
	Dim ITEM_AVAILABLE_HATS(ITEM_MAX(2))
	Dim ITEM_AVAILABLE_EGGS(ITEM_MAX(3))
	Dim ITEM_AVAILABLE_TOYS(ITEM_MAX(5))

	Function RandomizeDealersInventory()
		ITEM_AVAILABLE_FRUITS(1) = 1
		ITEM_AVAILABLE_FRUITS(2) = 1
		ITEM_AVAILABLE_FRUITS(3) = 1
		ITEM_AVAILABLE_FRUITS(4) = 1
		ITEM_AVAILABLE_HATS(1) = 1
		ITEM_AVAILABLE_EGGS(1) = 1
		ITEM_AVAILABLE_TOYS(1) = 1
		Repeat
			For i=5 to ITEM_MAX(1) : ITEM_AVAILABLE_FRUITS(i) = Rand(0,1) : Next
		Until(CountDealersInventory(1)>=4)
		Repeat
			For i=2 to ITEM_MAX(2) : ITEM_AVAILABLE_HATS(i) = Rand(0,1) : Next
		Until(CountDealersInventory(2)>=4)
		Repeat
			For i=2 to ITEM_MAX(3) : ITEM_AVAILABLE_EGGS(i) = Rand(0,1) : Next
		Until(CountDealersInventory(3)>=4)
		Repeat
			For i=2 to ITEM_MAX(5) : ITEM_AVAILABLE_TOYS(i) = Rand(0,1) : Next
		Until(CountDealersInventory(4)>=4)
	End Function

	Function CountDealersInventory(mode)
		j=0
		Select mode
			Case 1:
				For i=1 to ITEM_MAX(1)
					If ITEM_AVAILABLE_FRUITS(i)=1 Then j=j+1
				Next
			Case 2:
				For i=1 to ITEM_MAX(2)
					If ITEM_AVAILABLE_HATS(i)=1 Then j=j+1
				Next
			Case 3:
				For i=1 to ITEM_MAX(3)
					If ITEM_AVAILABLE_EGGS(i)=1 Then j=j+1
				Next
			Case 4:
				For i=1 to ITEM_MAX(5)
					If ITEM_AVAILABLE_TOYS(i)=1 Then j=j+1
				Next
		End Select
		Return j
	End Function

	Type tDealerItem
		Field ID
		Field Type1
	End Type
	Global TOTALDEALERITEMS

	Function DealerItem_Create.tDealerItem(id#, type1#)
		dii.tDealerItem = New tDealerItem
		TOTALDEALERITEMS=TOTALDEALERITEMS+1
		dii\ID=id#
		dii\Type1=type1#
		Return dii
	End Function

	Function FillDealersInventory(mode)
		For dii.tDealerItem = Each tDealerItem
			Delete dii
		Next
		TOTALDEALERITEMS=0

		Select mode
			Case 1:
				For i=1 to ITEM_MAX(mode)
					If ITEM_AVAILABLE_FRUITS(i)=1 Then dii.tDealerItem = DealerItem_Create(TOTALDEALERITEMS+1, i)
				Next
			Case 2:
				For i=1 to ITEM_MAX(mode)
					If ITEM_AVAILABLE_HATS(i)=1 Then dii.tDealerItem = DealerItem_Create(TOTALDEALERITEMS+1, i)
				Next
			Case 3:
				For i=1 to ITEM_MAX(mode)
					If ITEM_AVAILABLE_EGGS(i)=1 Then dii.tDealerItem = DealerItem_Create(TOTALDEALERITEMS+1, i)
				Next
			Case 5:
				For i=1 to ITEM_MAX(mode)
					If ITEM_AVAILABLE_TOYS(i)=1 Then dii.tDealerItem = DealerItem_Create(TOTALDEALERITEMS+1, i)
				Next
		End Select

	End Function

;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------

	fm = 1
	Global CHAOCOLOR_CELESTE= fm : fm=fm+1
	Global CHAOCOLOR_WHITE	= fm : fm=fm+1
	Global CHAOCOLOR_BLUE	= fm : fm=fm+1
	Global CHAOCOLOR_RED	= fm : fm=fm+1
	Global CHAOCOLOR_YELLOW	= fm : fm=fm+1
	Global CHAOCOLOR_ORANGE	= fm : fm=fm+1
	Global CHAOCOLOR_AZURE	= fm : fm=fm+1
	Global CHAOCOLOR_PINK	= fm : fm=fm+1
	Global CHAOCOLOR_PURPLE	= fm : fm=fm+1
	Global CHAOCOLOR_GREEN	= fm : fm=fm+1
	Global CHAOCOLOR_BROWN	= fm : fm=fm+1
	Global CHAOCOLOR_GREY	= fm : fm=fm+1
	Global CHAOCOLOR_LIME	= fm : fm=fm+1
	Global CHAOCOLOR_BLACK	= fm : fm=fm+1
	Global CHAOCOLORS_total = fm-1

	Dim CHAOCOLORS$(ITEM_MAX(3))
	CHAOCOLORS$(CHAOCOLOR_CELESTE)	= "Celeste"
	CHAOCOLORS$(CHAOCOLOR_AZURE)	= "Azure"
	CHAOCOLORS$(CHAOCOLOR_BLUE)	= "Blue"
	CHAOCOLORS$(CHAOCOLOR_WHITE)	= "White"
	CHAOCOLORS$(CHAOCOLOR_GREY)	= "Grey"
	CHAOCOLORS$(CHAOCOLOR_BLACK)	= "Black"
	CHAOCOLORS$(CHAOCOLOR_RED)	= "Red"
	CHAOCOLORS$(CHAOCOLOR_ORANGE)	= "Orange"
	CHAOCOLORS$(CHAOCOLOR_YELLOW)	= "Yellow"
	CHAOCOLORS$(CHAOCOLOR_BROWN)	= "Brown"
	CHAOCOLORS$(CHAOCOLOR_GREEN)	= "Green"
	CHAOCOLORS$(CHAOCOLOR_LIME)	= "Lime"
	CHAOCOLORS$(CHAOCOLOR_PURPLE)	= "Purple"
	CHAOCOLORS$(CHAOCOLOR_PINK)	= "Pink"

	Global CHAOSHAPE_NORMAL	= 1
	Global CHAOSHAPE_RUN	= 2
	Global CHAOSHAPE_SWIM	= 3
	Global CHAOSHAPE_FLY	= 4
	Global CHAOSHAPE_STRENGTH= 5

	Dim CHAOSHAPES$(5)
	CHAOSHAPES$(CHAOSHAPE_NORMAL)	= "Normal"
	CHAOSHAPES$(CHAOSHAPE_RUN)	= "Run"
	CHAOSHAPES$(CHAOSHAPE_SWIM)	= "Swim"
	CHAOSHAPES$(CHAOSHAPE_FLY)	= "Fly"
	CHAOSHAPES$(CHAOSHAPE_STRENGTH)	= "Strength"

	Global CHAOSIDE_HERO	= 1
	Global CHAOSIDE_DARK	= 2
	Global CHAOSIDE_NEUTRAL	= 3

	Dim CHAOSIDES$(3)
	CHAOSIDES$(CHAOSIDE_NEUTRAL)	= "Neutral"
	CHAOSIDES$(CHAOSIDE_HERO)	= "Hero"
	CHAOSIDES$(CHAOSIDE_DARK)	= "Dark"

;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------

	fm = 1
	Global FRUIT_ROUND	= fm : fm=fm+1
	Global FRUIT_CUBICLE	= fm : fm=fm+1
	Global FRUIT_TRIANGULAR	= fm : fm=fm+1
	Global FRUIT_HEART	= fm : fm=fm+1
	Global FRUIT_APPLE	= fm : fm=fm+1
	Global FRUIT_ORANGE	= fm : fm=fm+1
	Global FRUIT_BANANA	= fm : fm=fm+1
	Global FRUIT_PEAR	= fm : fm=fm+1
	Global FRUIT_GRAPE	= fm : fm=fm+1
	Global FRUIT_WATERMELON	= fm : fm=fm+1
	Global FRUIT_MANGO	= fm : fm=fm+1
	Global FRUIT_LEMON	= fm : fm=fm+1
	Global FRUIT_MANDARINE	= fm : fm=fm+1
	Global FRUIT_STRAWBERRY	= fm : fm=fm+1
	Global FRUIT_COCONUT	= fm : fm=fm+1
	Global FRUIT_PINEAPPLE	= fm : fm=fm+1
	Global FRUIT_KIWI	= fm : fm=fm+1
	Global FRUIT_APRICOT	= fm : fm=fm+1
	Global FRUIT_PEACH	= fm : fm=fm+1
	Global FRUIT_PITAYA	= fm : fm=fm+1
	Global FRUIT_CHERRY	= fm : fm=fm+1
	Global FRUIT_BLACKBERRY	= fm : fm=fm+1
	Global FRUIT_CARAMBOLA	= fm : fm=fm+1
	Global FRUIT_PLUM	= fm : fm=fm+1
	Global FRUIT_COBALT	= fm : fm=fm+1
	Global FRUIT_JUICY	= fm : fm=fm+1
	Global FRUIT_MUSHROOM	= fm : fm=fm+1
	Global FRUIT_CARROT	= fm : fm=fm+1
	Global FRUIT_PARSLEY	= fm : fm=fm+1
	Global FRUIT_TOMATO	= fm : fm=fm+1
	Global FRUIT_POTATO	= fm : fm=fm+1
	Global FRUIT_CORN	= fm : fm=fm+1
	Global FRUIT_PEPPER	= fm : fm=fm+1
	Global FRUIT_LETTUCE	= fm : fm=fm+1
	Global FRUIT_CUCUMBER	= fm : fm=fm+1
	Global FRUIT_ONION	= fm : fm=fm+1
	Global FRUIT_RADISH	= fm : fm=fm+1
	Global FRUIT_BREAD	= fm : fm=fm+1
	Global FRUIT_CHEESE	= fm : fm=fm+1
	Global FRUIT_PIZZA	= fm : fm=fm+1
	Global FRUIT_BURGER	= fm : fm=fm+1
	Global FRUIT_CHILI	= fm : fm=fm+1
	Global FRUIT_RICEBALL	= fm : fm=fm+1
	Global FRUIT_CANDY	= fm : fm=fm+1
	Global FRUIT_COOKIE	= fm : fm=fm+1
	Global FRUIT_ICECREAM	= fm : fm=fm+1
	Global FRUIT_POPSICLE	= fm : fm=fm+1
	Global FRUIT_MARSHMELLOW= fm : fm=fm+1
	Global FRUIT_CHOCOLATE	= fm : fm=fm+1
	Global FRUIT_PIE	= fm : fm=fm+1
	Global FRUIT_GOLDEN	= fm : fm=fm+1

	Dim FRUITS$(ITEM_MAX(1))
	FRUITS$(FRUIT_ROUND)		= "Round"
	FRUITS$(FRUIT_CUBICLE)		= "Cubicle"
	FRUITS$(FRUIT_TRIANGULAR)	= "Triangular"
	FRUITS$(FRUIT_HEART)		= "Heart"
	FRUITS$(FRUIT_APPLE)		= "Apple"
	FRUITS$(FRUIT_ORANGE)		= "Orange"
	FRUITS$(FRUIT_BANANA)		= "Banana"
	FRUITS$(FRUIT_PEAR)		= "Pear"
	FRUITS$(FRUIT_GRAPE)		= "Grape"
	FRUITS$(FRUIT_WATERMELON)	= "Watermelon"
	FRUITS$(FRUIT_MANGO)		= "Mango"
	FRUITS$(FRUIT_LEMON)		= "Lemon"
	FRUITS$(FRUIT_MANDARINE)	= "Mandarine"
	FRUITS$(FRUIT_STRAWBERRY)	= "Strawberry"
	FRUITS$(FRUIT_COCONUT)		= "Coconut"
	FRUITS$(FRUIT_PINEAPPLE)	= "Pineapple"
	FRUITS$(FRUIT_KIWI)		= "Kiwi"
	FRUITS$(FRUIT_APRICOT)		= "Apricot"
	FRUITS$(FRUIT_PEACH)		= "Peach"
	FRUITS$(FRUIT_PITAYA)		= "Pitaya"
	FRUITS$(FRUIT_CHERRY)		= "Cherry"
	FRUITS$(FRUIT_BLACKBERRY)	= "Blackberry"
	FRUITS$(FRUIT_CARAMBOLA)	= "Carambola"
	FRUITS$(FRUIT_PLUM)		= "Plum"
	FRUITS$(FRUIT_COBALT)		= "Cobalt"
	FRUITS$(FRUIT_JUICY)		= "Juicy"
	FRUITS$(FRUIT_MUSHROOM)		= "Mushroom"
	FRUITS$(FRUIT_CARROT)		= "Carrot"
	FRUITS$(FRUIT_PARSLEY)		= "Parsley"
	FRUITS$(FRUIT_TOMATO)		= "Tomato"
	FRUITS$(FRUIT_POTATO)		= "Potato"
	FRUITS$(FRUIT_CORN)		= "Corn"
	FRUITS$(FRUIT_PEPPER)		= "Pepper"
	FRUITS$(FRUIT_LETTUCE)		= "Lettuce"
	FRUITS$(FRUIT_CUCUMBER)		= "Cucumber"
	FRUITS$(FRUIT_ONION)		= "Onion"
	FRUITS$(FRUIT_RADISH)		= "Radish"
	FRUITS$(FRUIT_BREAD)		= "Bread"
	FRUITS$(FRUIT_CHEESE)		= "Cheddar"
	FRUITS$(FRUIT_PIZZA)		= "Pizza"
	FRUITS$(FRUIT_BURGER)		= "Burger"
	FRUITS$(FRUIT_CHILI)		= "Chili dog"
	FRUITS$(FRUIT_RICEBALL)		= "Rice ball"
	FRUITS$(FRUIT_CANDY)		= "Candy cane"
	FRUITS$(FRUIT_COOKIE)		= "Cookie"
	FRUITS$(FRUIT_ICECREAM)		= "Ice cream"
	FRUITS$(FRUIT_POPSICLE)		= "Popsicle"
	FRUITS$(FRUIT_MARSHMELLOW)	= "Marshmellow"
	FRUITS$(FRUIT_CHOCOLATE)	= "Chocolate"
	FRUITS$(FRUIT_PIE)		= "Pie"
	FRUITS$(FRUIT_GOLDEN)		= "Golden"

	Dim FRUITS_TYPE$(ITEM_MAX(1))
	FRUITS_TYPE$(FRUIT_ROUND)	= "fruit"
	FRUITS_TYPE$(FRUIT_CUBICLE)	= "fruit"
	FRUITS_TYPE$(FRUIT_TRIANGULAR)	= "fruit"
	FRUITS_TYPE$(FRUIT_HEART)	= "fruit"
	FRUITS_TYPE$(FRUIT_APPLE)	= "fruit"
	FRUITS_TYPE$(FRUIT_ORANGE)	= "fruit"
	FRUITS_TYPE$(FRUIT_BANANA)	= "fruit"
	FRUITS_TYPE$(FRUIT_PEAR)	= "fruit"
	FRUITS_TYPE$(FRUIT_GRAPE)	= "fruit"
	FRUITS_TYPE$(FRUIT_WATERMELON)	= "fruit"
	FRUITS_TYPE$(FRUIT_MANGO)	= "fruit"
	FRUITS_TYPE$(FRUIT_LEMON)	= "fruit"
	FRUITS_TYPE$(FRUIT_MANDARINE)	= "fruit"
	FRUITS_TYPE$(FRUIT_STRAWBERRY)	= "fruit"
	FRUITS_TYPE$(FRUIT_COCONUT)	= "fruit"
	FRUITS_TYPE$(FRUIT_PINEAPPLE)	= "fruit"
	FRUITS_TYPE$(FRUIT_KIWI)	= "fruit"
	FRUITS_TYPE$(FRUIT_APRICOT)	= "fruit"
	FRUITS_TYPE$(FRUIT_PEACH)	= "fruit"
	FRUITS_TYPE$(FRUIT_PITAYA)	= "fruit"
	FRUITS_TYPE$(FRUIT_CHERRY)	= "fruit"
	FRUITS_TYPE$(FRUIT_BLACKBERRY)	= "fruit"
	FRUITS_TYPE$(FRUIT_CARAMBOLA)	= "fruit"
	FRUITS_TYPE$(FRUIT_PLUM)	= "fruit"
	FRUITS_TYPE$(FRUIT_COBALT)	= "fruit"
	FRUITS_TYPE$(FRUIT_JUICY)	= "fruit"
	FRUITS_TYPE$(FRUIT_MUSHROOM)	= ""
	FRUITS_TYPE$(FRUIT_CARROT)	= "veggie"
	FRUITS_TYPE$(FRUIT_PARSLEY)	= "veggie"
	FRUITS_TYPE$(FRUIT_TOMATO)	= "veggie"
	FRUITS_TYPE$(FRUIT_POTATO)	= "veggie"
	FRUITS_TYPE$(FRUIT_CORN)	= "veggie"
	FRUITS_TYPE$(FRUIT_PEPPER)	= "veggie"
	FRUITS_TYPE$(FRUIT_LETTUCE)	= "veggie"
	FRUITS_TYPE$(FRUIT_CUCUMBER)	= "veggie"
	FRUITS_TYPE$(FRUIT_ONION)	= "veggie"
	FRUITS_TYPE$(FRUIT_RADISH)	= "veggie"
	FRUITS_TYPE$(FRUIT_BREAD)	= "loaf"
	FRUITS_TYPE$(FRUIT_CHEESE)	= "cheese"
	FRUITS_TYPE$(FRUIT_PIZZA)	= ""
	FRUITS_TYPE$(FRUIT_BURGER)	= ""
	FRUITS_TYPE$(FRUIT_CHILI)	= ""
	FRUITS_TYPE$(FRUIT_RICEBALL)	= ""
	FRUITS_TYPE$(FRUIT_CANDY)	= ""
	FRUITS_TYPE$(FRUIT_COOKIE)	= ""
	FRUITS_TYPE$(FRUIT_ICECREAM)	= "cone"
	FRUITS_TYPE$(FRUIT_POPSICLE)	= ""
	FRUITS_TYPE$(FRUIT_MARSHMELLOW)	= ""
	FRUITS_TYPE$(FRUIT_CHOCOLATE)	= "bar"
	FRUITS_TYPE$(FRUIT_PIE)		= "cake"
	FRUITS_TYPE$(FRUIT_GOLDEN)	= "fruit"

;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------

	fm = 1
	Global SHELL_BOTTOM		= fm : fm=fm+1
	Global SHELL_TOP		= fm : fm=fm+1

	Dim SHELLS$(ITEM_MAX(4))
	SHELLS$(SHELL_BOTTOM)		= "Egg shell"
	SHELLS$(SHELL_TOP)		= "Egg shell"

;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------

	fm = 1
	Global HAT_CAT_0	= fm : fm=fm+1
	Global HAT_CAT_1	= fm : fm=fm+1
	Global HAT_CAT_2	= fm : fm=fm+1
	Global HAT_CAT_3	= fm : fm=fm+1
	Global HAT_STRAW_0	= fm : fm=fm+1
	Global HAT_WOOL_0	= fm : fm=fm+1
	Global HAT_WOOL_1	= fm : fm=fm+1
	Global HAT_WOOL_2	= fm : fm=fm+1
	Global HAT_WOOL_3	= fm : fm=fm+1
	Global HAT_FORMAL_0	= fm : fm=fm+1
	Global HAT_FORMAL_1	= fm : fm=fm+1
	Global HAT_SKULL_0	= fm : fm=fm+1
	Global HAT_BUCKET_0	= fm : fm=fm+1
	Global HAT_TIE_0	= fm : fm=fm+1
	Global HAT_TIE_1	= fm : fm=fm+1
	Global HAT_TIE_2	= fm : fm=fm+1
	Global HAT_TIE_3	= fm : fm=fm+1
	Global HAT_BOW_0	= fm : fm=fm+1
	Global HAT_BOW_1	= fm : fm=fm+1
	Global HAT_BOW_2	= fm : fm=fm+1
	Global HAT_BOW_3	= fm : fm=fm+1
	Global HAT_TUNIC_0	= fm : fm=fm+1
	Global HAT_TUNIC_1	= fm : fm=fm+1
	Global HAT_TUNIC_2	= fm : fm=fm+1
	Global HAT_TUNIC_3	= fm : fm=fm+1
	Global HAT_BEANIE_0	= fm : fm=fm+1
	Global HAT_BEANIE_1	= fm : fm=fm+1
	Global HAT_BEANIE_2	= fm : fm=fm+1
	Global HAT_BEANIE_3	= fm : fm=fm+1
	Global HAT_FPOT_0	= fm : fm=fm+1
	Global HAT_FCROWN_0	= fm : fm=fm+1
	Global HAT_SGLASSES_0	= fm : fm=fm+1
	Global HAT_PACIFIER_0	= fm : fm=fm+1
	Global HAT_PACIFIER_1	= fm : fm=fm+1
	Global HAT_PACIFIER_2	= fm : fm=fm+1
	Global HAT_PACIFIER_3	= fm : fm=fm+1
	Global HAT_CARDBOARD_0	= fm : fm=fm+1
	Global HAT_SHELL_0	= fm : fm=fm+1
	Global HAT_BUNNY_0	= fm : fm=fm+1
	Global HAT_BUNNY_1	= fm : fm=fm+1
	Global HAT_STACHE_0	= fm : fm=fm+1
	Global HAT_HOCKEY_0	= fm : fm=fm+1
	Global HAT_TOPHAT_0	= fm : fm=fm+1
	Global HAT_GLASSES_0	= fm : fm=fm+1
	Global HAT_GLASSES_1	= fm : fm=fm+1
	Global HAT_GLASSES_2	= fm : fm=fm+1
	Global HAT_GLASSES_3	= fm : fm=fm+1
	Global HAT_HEADPHONES_0	= fm : fm=fm+1
	Global HAT_HEADPHONES_1	= fm : fm=fm+1
	Global HAT_PLUMBER_0	= fm : fm=fm+1
	Global HAT_PLUMBER_1	= fm : fm=fm+1
	Global HAT_PLUMBER_2	= fm : fm=fm+1
	Global HAT_PLUMBER_3	= fm : fm=fm+1
	Global HAT_PLUMBER_4	= fm : fm=fm+1
	Global HAT_BANDANA_0	= fm : fm=fm+1
	Global HAT_BANDANA_1	= fm : fm=fm+1
	Global HAT_BANDANA_2	= fm : fm=fm+1
	Global HAT_BANDANA_3	= fm : fm=fm+1
	Global HAT_BANDANA_4	= fm : fm=fm+1
	Global HAT_HEADBAND_0	= fm : fm=fm+1
	Global HAT_HEADBAND_1	= fm : fm=fm+1
	Global HAT_HEADBAND_2	= fm : fm=fm+1
	Global HAT_HEADBAND_3	= fm : fm=fm+1
	Global HAT_HEADBAND_4	= fm : fm=fm+1

	Dim HATS$(ITEM_MAX(2))
	HATS$(HAT_CAT_0)	= "Cat ears"
	HATS$(HAT_CAT_1)	= "Cat ears"
	HATS$(HAT_CAT_2)	= "Cat ears"
	HATS$(HAT_CAT_3)	= "Cat ears"
	HATS$(HAT_STRAW_0)	= "Straw hat"
	HATS$(HAT_WOOL_0)	= "Wool hat"
	HATS$(HAT_WOOL_1)	= "Wool hat"
	HATS$(HAT_WOOL_2)	= "Wool hat"
	HATS$(HAT_WOOL_3)	= "Wool hat"
	HATS$(HAT_FORMAL_0)	= "Formal hat"
	HATS$(HAT_FORMAL_1)	= "Formal hat"
	HATS$(HAT_SKULL_0)	= "Skull"
	HATS$(HAT_BUCKET_0)	= "Bucket"
	HATS$(HAT_TIE_0)	= "Neck tie"
	HATS$(HAT_TIE_1)	= "Neck tie"
	HATS$(HAT_TIE_2)	= "Neck tie"
	HATS$(HAT_TIE_3)	= "Neck tie"
	HATS$(HAT_BOW_0)	= "Bow tie"
	HATS$(HAT_BOW_1)	= "Bow tie"
	HATS$(HAT_BOW_2)	= "Bow tie"
	HATS$(HAT_BOW_3)	= "Bow tie"
	HATS$(HAT_TUNIC_0)	= "Tunic hat"
	HATS$(HAT_TUNIC_1)	= "Tunic hat"
	HATS$(HAT_TUNIC_2)	= "Tunic hat"
	HATS$(HAT_TUNIC_3)	= "Tunic hat"
	HATS$(HAT_BEANIE_0)	= "Beanie"
	HATS$(HAT_BEANIE_1)	= "Beanie"
	HATS$(HAT_BEANIE_2)	= "Beanie"
	HATS$(HAT_BEANIE_3)	= "Beanie"
	HATS$(HAT_FPOT_0)	= "Flower pot"
	HATS$(HAT_FCROWN_0)	= "Flower crown"
	HATS$(HAT_SGLASSES_0)	= "Sunglasses"
	HATS$(HAT_PACIFIER_0)	= "Pacifier"
	HATS$(HAT_PACIFIER_1)	= "Pacifier"
	HATS$(HAT_PACIFIER_2)	= "Pacifier"
	HATS$(HAT_PACIFIER_3)	= "Pacifier"
	HATS$(HAT_CARDBOARD_0)	= "Cardboard box"
	HATS$(HAT_SHELL_0)	= "Broken shell"
	HATS$(HAT_BUNNY_0)	= "Bunny ears"
	HATS$(HAT_BUNNY_1)	= "Bunny ears"
	HATS$(HAT_STACHE_0)	= "Fake moustache"
	HATS$(HAT_HOCKEY_0)	= "Hockey mask"
	HATS$(HAT_TOPHAT_0)	= "Top hat"
	HATS$(HAT_GLASSES_0)	= "Glasses"
	HATS$(HAT_GLASSES_1)	= "Glasses"
	HATS$(HAT_GLASSES_2)	= "Glasses"
	HATS$(HAT_GLASSES_3)	= "Glasses"
	HATS$(HAT_HEADPHONES_0)	= "Headphones"
	HATS$(HAT_HEADPHONES_1)	= "Headphones"
	HATS$(HAT_PLUMBER_0)	= "Plumber cap"
	HATS$(HAT_PLUMBER_1)	= "Plumber cap"
	HATS$(HAT_PLUMBER_2)	= "Plumber cap"
	HATS$(HAT_PLUMBER_3)	= "Plumber cap"
	HATS$(HAT_PLUMBER_4)	= "Plumber cap"
	HATS$(HAT_BANDANA_0)	= "Bandana"
	HATS$(HAT_BANDANA_1)	= "Bandana"
	HATS$(HAT_BANDANA_2)	= "Bandana"
	HATS$(HAT_BANDANA_3)	= "Bandana"
	HATS$(HAT_BANDANA_4)	= "Bandana"
	HATS$(HAT_HEADBAND_0)	= "Headband"
	HATS$(HAT_HEADBAND_1)	= "Headband"
	HATS$(HAT_HEADBAND_2)	= "Headband"
	HATS$(HAT_HEADBAND_3)	= "Headband"
	HATS$(HAT_HEADBAND_4)	= "Headband"

	Dim HATS_FILE$(ITEM_MAX(2))
	HATS_FILE$(HAT_CAT_0)		= "CatEars0"
	HATS_FILE$(HAT_CAT_1)		= "CatEars1"
	HATS_FILE$(HAT_CAT_2)		= "CatEars2"
	HATS_FILE$(HAT_CAT_3)		= "CatEars3"
	HATS_FILE$(HAT_STRAW_0)		= "StrawHat0"
	HATS_FILE$(HAT_WOOL_0)		= "WoolHat0"
	HATS_FILE$(HAT_WOOL_1)		= "WoolHat1"
	HATS_FILE$(HAT_WOOL_2)		= "WoolHat2"
	HATS_FILE$(HAT_WOOL_3)		= "WoolHat3"
	HATS_FILE$(HAT_FORMAL_0)	= "FormalHat0"
	HATS_FILE$(HAT_FORMAL_1)	= "FormalHat1"
	HATS_FILE$(HAT_SKULL_0)		= "Skull0"
	HATS_FILE$(HAT_BUCKET_0)	= "Bucket0"
	HATS_FILE$(HAT_TIE_0)		= "NeckTie0"
	HATS_FILE$(HAT_TIE_1)		= "NeckTie1"
	HATS_FILE$(HAT_TIE_2)		= "NeckTie2"
	HATS_FILE$(HAT_TIE_3)		= "NeckTie3"
	HATS_FILE$(HAT_BOW_0)		= "BowTie0"
	HATS_FILE$(HAT_BOW_1)		= "BowTie1"
	HATS_FILE$(HAT_BOW_2)		= "BowTie2"
	HATS_FILE$(HAT_BOW_3)		= "BowTie3"
	HATS_FILE$(HAT_TUNIC_0)		= "TunicHat0"
	HATS_FILE$(HAT_TUNIC_1)		= "TunicHat1"
	HATS_FILE$(HAT_TUNIC_2)		= "TunicHat2"
	HATS_FILE$(HAT_TUNIC_3)		= "TunicHat3"
	HATS_FILE$(HAT_BEANIE_0)	= "Beanie0"
	HATS_FILE$(HAT_BEANIE_1)	= "Beanie1"
	HATS_FILE$(HAT_BEANIE_2)	= "Beanie2"
	HATS_FILE$(HAT_BEANIE_3)	= "Beanie3"
	HATS_FILE$(HAT_FPOT_0)		= "FlowerPot0"
	HATS_FILE$(HAT_FCROWN_0)	= "FlowerCrown0"
	HATS_FILE$(HAT_SGLASSES_0)	= "BeachGlasses0"
	HATS_FILE$(HAT_PACIFIER_0)	= "Pacifier0"
	HATS_FILE$(HAT_PACIFIER_1)	= "Pacifier1"
	HATS_FILE$(HAT_PACIFIER_2)	= "Pacifier2"
	HATS_FILE$(HAT_PACIFIER_3)	= "Pacifier3"
	HATS_FILE$(HAT_CARDBOARD_0)	= "CardboardBox0"
	HATS_FILE$(HAT_SHELL_0)		= "BrokenShell0"
	HATS_FILE$(HAT_BUNNY_0)		= "BunnyEars0"
	HATS_FILE$(HAT_BUNNY_1)		= "BunnyEars1"
	HATS_FILE$(HAT_STACHE_0)	= "FakeMoustache0"
	HATS_FILE$(HAT_HOCKEY_0)	= "HockeyMask0"
	HATS_FILE$(HAT_TOPHAT_0)	= "TopHat0"
	HATS_FILE$(HAT_GLASSES_0)	= "GeekGlasses0"
	HATS_FILE$(HAT_GLASSES_1)	= "GeekGlasses1"
	HATS_FILE$(HAT_GLASSES_2)	= "GeekGlasses2"
	HATS_FILE$(HAT_GLASSES_3)	= "GeekGlasses3"
	HATS_FILE$(HAT_HEADPHONES_0)	= "Headphones0"
	HATS_FILE$(HAT_HEADPHONES_1)	= "Headphones1"
	HATS_FILE$(HAT_PLUMBER_0)	= "PlumberCap0"
	HATS_FILE$(HAT_PLUMBER_1)	= "PlumberCap1"
	HATS_FILE$(HAT_PLUMBER_2)	= "PlumberCap2"
	HATS_FILE$(HAT_PLUMBER_3)	= "PlumberCap3"
	HATS_FILE$(HAT_PLUMBER_4)	= "PlumberCap4"
	HATS_FILE$(HAT_BANDANA_0)	= "Bandana0"
	HATS_FILE$(HAT_BANDANA_1)	= "Bandana1"
	HATS_FILE$(HAT_BANDANA_2)	= "Bandana2"
	HATS_FILE$(HAT_BANDANA_3)	= "Bandana3"
	HATS_FILE$(HAT_BANDANA_4)	= "Bandana4"
	HATS_FILE$(HAT_HEADBAND_0)	= "Headband0"
	HATS_FILE$(HAT_HEADBAND_1)	= "Headband1"
	HATS_FILE$(HAT_HEADBAND_2)	= "Headband2"
	HATS_FILE$(HAT_HEADBAND_3)	= "Headband3"
	HATS_FILE$(HAT_HEADBAND_4)	= "Headband4"

;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------

	fm = 1
	Global TOY_BEACHBALL	= fm : fm=fm+1
	Global TOY_SOCCERBALL	= fm : fm=fm+1
	Global TOY_TEDDYBEAR	= fm : fm=fm+1
	Global TOY_MICROPHONE	= fm : fm=fm+1
	Global TOY_DUMBBELL1	= fm : fm=fm+1
	Global TOY_DUMBBELL2	= fm : fm=fm+1
	Global TOY_DUMBBELL3	= fm : fm=fm+1
	Global TOY_DUMBBELL4	= fm : fm=fm+1
	Global TOY_DUMBBELL5	= fm : fm=fm+1
	Global TOY_RUBBERDUCK	= fm : fm=fm+1
	Global TOY_RATTLE	= fm : fm=fm+1
	Global TOY_TOYCAR	= fm : fm=fm+1
	Global TOY_PILLOW	= fm : fm=fm+1

	Dim TOYS$(ITEM_MAX(5))
	TOYS$(TOY_BEACHBALL)	= "Beach ball"
	TOYS$(TOY_SOCCERBALL)	= "Soccer ball"
	TOYS$(TOY_TEDDYBEAR)	= "Teddy bear"
	TOYS$(TOY_MICROPHONE)	= "Microphone"
	TOYS$(TOY_DUMBBELL1)	= "Dumbbell unit 1"
	TOYS$(TOY_DUMBBELL2)	= "Dumbbell unit 2"
	TOYS$(TOY_DUMBBELL3)	= "Dumbbell unit 3"
	TOYS$(TOY_DUMBBELL4)	= "Dumbbell unit 4"
	TOYS$(TOY_DUMBBELL5)	= "Dumbbell unit 5"
	TOYS$(TOY_RUBBERDUCK)	= "Rubber duck"
	TOYS$(TOY_RATTLE)	= "Rattle"
	TOYS$(TOY_TOYCAR)	= "Toy car"
	TOYS$(TOY_PILLOW)	= "Pillow"

	Dim TOYS_FILE$(ITEM_MAX(5))
	TOYS_FILE$(TOY_BEACHBALL)	= "BeachBall"
	TOYS_FILE$(TOY_SOCCERBALL)	= "SoccerBall"
	TOYS_FILE$(TOY_TEDDYBEAR)	= "TeddyBear"
	TOYS_FILE$(TOY_MICROPHONE)	= "Microphone"
	TOYS_FILE$(TOY_DUMBBELL1)	= "Dumbbell1"
	TOYS_FILE$(TOY_DUMBBELL2)	= "Dumbbell2"
	TOYS_FILE$(TOY_DUMBBELL3)	= "Dumbbell3"
	TOYS_FILE$(TOY_DUMBBELL4)	= "Dumbbell4"
	TOYS_FILE$(TOY_DUMBBELL5)	= "Dumbbell5"
	TOYS_FILE$(TOY_RUBBERDUCK)	= "RubberDuck"
	TOYS_FILE$(TOY_RATTLE)		= "Rattle"
	TOYS_FILE$(TOY_TOYCAR)		= "ToyCar"
	TOYS_FILE$(TOY_PILLOW)		= "Pillow"

;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------

	Dim FRUITS_PRICES(ITEM_MAX(1))
	FRUITS_PRICES(FRUIT_ROUND)	= 5
	FRUITS_PRICES(FRUIT_CUBICLE)	= 10
	FRUITS_PRICES(FRUIT_TRIANGULAR)	= 10
	FRUITS_PRICES(FRUIT_HEART)	= 60
	FRUITS_PRICES(FRUIT_APPLE)	= 20
	FRUITS_PRICES(FRUIT_ORANGE)	= 20
	FRUITS_PRICES(FRUIT_BANANA)	= 30
	FRUITS_PRICES(FRUIT_PEAR)	= 20
	FRUITS_PRICES(FRUIT_GRAPE)	= 30
	FRUITS_PRICES(FRUIT_WATERMELON)	= 40
	FRUITS_PRICES(FRUIT_MANGO)	= 20
	FRUITS_PRICES(FRUIT_LEMON)	= 10
	FRUITS_PRICES(FRUIT_MANDARINE)	= 15
	FRUITS_PRICES(FRUIT_STRAWBERRY)	= 15
	FRUITS_PRICES(FRUIT_COCONUT)	= 30
	FRUITS_PRICES(FRUIT_PINEAPPLE)	= 20
	FRUITS_PRICES(FRUIT_KIWI)	= 10
	FRUITS_PRICES(FRUIT_APRICOT)	= 20
	FRUITS_PRICES(FRUIT_PEACH)	= 20
	FRUITS_PRICES(FRUIT_PITAYA)	= 40
	FRUITS_PRICES(FRUIT_CHERRY)	= 15
	FRUITS_PRICES(FRUIT_BLACKBERRY)	= 20
	FRUITS_PRICES(FRUIT_CARAMBOLA)	= 25
	FRUITS_PRICES(FRUIT_PLUM)	= 15
	FRUITS_PRICES(FRUIT_COBALT)	= 10
	FRUITS_PRICES(FRUIT_JUICY)	= 40
	FRUITS_PRICES(FRUIT_MUSHROOM)	= 20
	FRUITS_PRICES(FRUIT_CARROT)	= 15
	FRUITS_PRICES(FRUIT_PARSLEY)	= 20
	FRUITS_PRICES(FRUIT_TOMATO)	= 15
	FRUITS_PRICES(FRUIT_POTATO)	= 10
	FRUITS_PRICES(FRUIT_CORN)	= 25
	FRUITS_PRICES(FRUIT_PEPPER)	= 10
	FRUITS_PRICES(FRUIT_LETTUCE)	= 20
	FRUITS_PRICES(FRUIT_CUCUMBER)	= 15
	FRUITS_PRICES(FRUIT_ONION)	= 15
	FRUITS_PRICES(FRUIT_RADISH)	= 20
	FRUITS_PRICES(FRUIT_BREAD)	= 20
	FRUITS_PRICES(FRUIT_CHEESE)	= 30
	FRUITS_PRICES(FRUIT_PIZZA)	= 20
	FRUITS_PRICES(FRUIT_BURGER)	= 15
	FRUITS_PRICES(FRUIT_CHILI)	= 20
	FRUITS_PRICES(FRUIT_RICEBALL)	= 20
	FRUITS_PRICES(FRUIT_CANDY)	= 30
	FRUITS_PRICES(FRUIT_COOKIE)	= 20
	FRUITS_PRICES(FRUIT_ICECREAM)	= 35
	FRUITS_PRICES(FRUIT_POPSICLE)	= 10
	FRUITS_PRICES(FRUIT_MARSHMELLOW)= 10
	FRUITS_PRICES(FRUIT_CHOCOLATE)	= 25
	FRUITS_PRICES(FRUIT_PIE)	= 30
	FRUITS_PRICES(FRUIT_GOLDEN)	= 1500

	Dim EGGS_PRICES(ITEM_MAX(3))
	EGGS_PRICES(CHAOCOLOR_CELESTE)	= 100
	EGGS_PRICES(CHAOCOLOR_AZURE)	= 500
	EGGS_PRICES(CHAOCOLOR_BLUE)	= 400
	EGGS_PRICES(CHAOCOLOR_WHITE)	= 300
	EGGS_PRICES(CHAOCOLOR_GREY)	= 800
	EGGS_PRICES(CHAOCOLOR_BLACK)	= 1000
	EGGS_PRICES(CHAOCOLOR_RED)	= 400
	EGGS_PRICES(CHAOCOLOR_ORANGE)	= 500
	EGGS_PRICES(CHAOCOLOR_YELLOW)	= 500
	EGGS_PRICES(CHAOCOLOR_BROWN)	= 600
	EGGS_PRICES(CHAOCOLOR_GREEN)	= 300
	EGGS_PRICES(CHAOCOLOR_LIME)	= 500
	EGGS_PRICES(CHAOCOLOR_PURPLE)	= 400
	EGGS_PRICES(CHAOCOLOR_PINK)	= 600

	Dim SHELLS_PRICES(ITEM_MAX(4))
	SHELLS_PRICES(SHELL_BOTTOM)	= 15
	SHELLS_PRICES(SHELL_TOP)	= 15

	Dim HATS_PRICES(ITEM_MAX(2))
	HATS_PRICES(HAT_CAT_0)		= 100
	HATS_PRICES(HAT_CAT_1)		= 100
	HATS_PRICES(HAT_CAT_2)		= 100
	HATS_PRICES(HAT_CAT_3)		= 100
	HATS_PRICES(HAT_STRAW_0)	= 400
	HATS_PRICES(HAT_WOOL_0)		= 500
	HATS_PRICES(HAT_WOOL_1)		= 500
	HATS_PRICES(HAT_WOOL_2)		= 500
	HATS_PRICES(HAT_WOOL_3)		= 500
	HATS_PRICES(HAT_FORMAL_0)	= 300
	HATS_PRICES(HAT_FORMAL_1)	= 300
	HATS_PRICES(HAT_SKULL_0)	= 600
	HATS_PRICES(HAT_BUCKET_0)	= 300
	HATS_PRICES(HAT_TIE_0)		= 200
	HATS_PRICES(HAT_TIE_1)		= 200
	HATS_PRICES(HAT_TIE_2)		= 200
	HATS_PRICES(HAT_TIE_3)		= 200
	HATS_PRICES(HAT_BOW_0)		= 70
	HATS_PRICES(HAT_BOW_1)		= 70
	HATS_PRICES(HAT_BOW_2)		= 70
	HATS_PRICES(HAT_BOW_3)		= 70
	HATS_PRICES(HAT_TUNIC_0)	= 300
	HATS_PRICES(HAT_TUNIC_1)	= 300
	HATS_PRICES(HAT_TUNIC_2)	= 300
	HATS_PRICES(HAT_TUNIC_3)	= 300
	HATS_PRICES(HAT_BEANIE_0)	= 200
	HATS_PRICES(HAT_BEANIE_1)	= 200
	HATS_PRICES(HAT_BEANIE_2)	= 200
	HATS_PRICES(HAT_BEANIE_3)	= 200
	HATS_PRICES(HAT_FPOT_0)		= 100
	HATS_PRICES(HAT_FCROWN_0)	= 400
	HATS_PRICES(HAT_SGLASSES_0)	= 100
	HATS_PRICES(HAT_PACIFIER_0)	= 50
	HATS_PRICES(HAT_PACIFIER_1)	= 50
	HATS_PRICES(HAT_PACIFIER_2)	= 50
	HATS_PRICES(HAT_PACIFIER_3)	= 50
	HATS_PRICES(HAT_CARDBOARD_0)	= 20
	HATS_PRICES(HAT_SHELL_0)	= 10
	HATS_PRICES(HAT_BUNNY_0)	= 300
	HATS_PRICES(HAT_BUNNY_1)	= 300
	HATS_PRICES(HAT_STACHE_0)	= 100
	HATS_PRICES(HAT_HOCKEY_0)	= 600
	HATS_PRICES(HAT_TOPHAT_0)	= 500
	HATS_PRICES(HAT_GLASSES_0)	= 200
	HATS_PRICES(HAT_GLASSES_1)	= 200
	HATS_PRICES(HAT_GLASSES_2)	= 200
	HATS_PRICES(HAT_GLASSES_3)	= 200
	HATS_PRICES(HAT_HEADPHONES_0)	= 500
	HATS_PRICES(HAT_HEADPHONES_1)	= 500
	HATS_PRICES(HAT_PLUMBER_0)	= 600
	HATS_PRICES(HAT_PLUMBER_1)	= 600
	HATS_PRICES(HAT_PLUMBER_2)	= 600
	HATS_PRICES(HAT_PLUMBER_3)	= 600
	HATS_PRICES(HAT_PLUMBER_4)	= 600
	HATS_PRICES(HAT_BANDANA_0)	= 100
	HATS_PRICES(HAT_BANDANA_1)	= 100
	HATS_PRICES(HAT_BANDANA_2)	= 100
	HATS_PRICES(HAT_BANDANA_3)	= 100
	HATS_PRICES(HAT_BANDANA_4)	= 100
	HATS_PRICES(HAT_HEADBAND_0)	= 200
	HATS_PRICES(HAT_HEADBAND_1)	= 200
	HATS_PRICES(HAT_HEADBAND_2)	= 200
	HATS_PRICES(HAT_HEADBAND_3)	= 200
	HATS_PRICES(HAT_HEADBAND_4)	= 200

	Dim TOYS_PRICES(ITEM_MAX(5))
	TOYS_PRICES(TOY_BEACHBALL)	= 200
	TOYS_PRICES(TOY_SOCCERBALL)	= 300
	TOYS_PRICES(TOY_TEDDYBEAR)	= 150
	TOYS_PRICES(TOY_MICROPHONE)	= 350
	TOYS_PRICES(TOY_DUMBBELL1)	= 200
	TOYS_PRICES(TOY_DUMBBELL2)	= 250
	TOYS_PRICES(TOY_DUMBBELL3)	= 300
	TOYS_PRICES(TOY_DUMBBELL4)	= 350
	TOYS_PRICES(TOY_DUMBBELL5)	= 400
	TOYS_PRICES(TOY_RUBBERDUCK)	= 100
	TOYS_PRICES(TOY_RATTLE)		= 250
	TOYS_PRICES(TOY_TOYCAR)		= 500
	TOYS_PRICES(TOY_PILLOW)		= 200

;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------
;--------------------------------------------------------------------------------------------------------------------------