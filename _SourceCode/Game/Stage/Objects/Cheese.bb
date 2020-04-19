
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	Type tCheese
		Field Position.tVector
		Field Entity
		Field Mesh
		Field Animation
		Field PreviousAnimation
		Field Channel_Chao
		Field targetp.tPlayer
		Field Emo.tChaoEmo
		Field Particle.tParticleTemplate
		Field ShadowCircle
	End Type

; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
	
	Function Object_Cheese_Create.tCheese()
	
		ch.tCheese = New tCheese

		ch\Position = New tVector
		
		ch\Entity = CreatePivot()
		LoadCharacterMesh(CHAR_CHE)
		ch\Mesh=CopyEntity(CharacterMesh, Game\Stage\Root)
		DeleteCharacterMesh()
		EntityType(ch\Entity,COLLISION_OBJECT2_GOTHRU)

		ch\Emo = Object_ChaoEmo_Create.tChaoEmo(ch\Mesh,CHAOSIDE_NEUTRAL,true)

		TranslateEntity ch\Entity, Game\Gameplay\CheckX#,Game\Gameplay\CheckY#+7,Game\Gameplay\CheckZ#

		If Menu\Settings\Shadows#>0 Then ch\ShadowCircle = Init_CircleShadow(ch\Entity, ch\Mesh, 1.5)

		ch\Particle = ParticleTemplate_Create.tParticleTemplate()
		
		Return ch
		
	End Function
	
	; =========================================================================================================

	Function Object_Cheese_Update(ch.tCheese, d.tDeltaTime)

		Select Menu\Members
			Case 1:
				If pp(1)\Character=CHAR_CRE Then
					ch\targetp=pp(1)
				Else
					ch\targetp=pp(1)
				EndIf
			Case 2:
				If pp(1)\Character=CHAR_CRE Then
					ch\targetp=pp(1)
				ElseIf pp(2)\Character=CHAR_CRE Then
					ch\targetp=pp(2)
				Else
					ch\targetp=pp(1)
				EndIf
			Case 3:
				If pp(1)\Character=CHAR_CRE Then
					ch\targetp=pp(1)
				ElseIf pp(2)\Character=CHAR_CRE Then
					ch\targetp=pp(2)
				ElseIf pp(3)\Character=CHAR_CRE Then
					ch\targetp=pp(3)
				Else
					ch\targetp=pp(1)
				EndIf
		End Select

		If ch\targetp\Character=CHAR_CRE and (Menu\ChaoGarden=0 Or Menu\Stage=999) Then
			ShowEntity(ch\Mesh)
			Object_Cheese_Update_Real(ch, ch\targetp, d)
			If Menu\Settings\Shadows#>0 Then Update_CircleShadow(ch\ShadowCircle, ch\Mesh, cam\Entity, 0)
		Else
			HideEntity(ch\Mesh)
			If Menu\Settings\Shadows#>0 Then Update_CircleShadow(ch\ShadowCircle, ch\Mesh, cam\Entity, 1)
		EndIf
		
	End Function

	Function Object_Cheese_Update_Real(ch.tCheese, p.tPlayer, d.tDeltaTime)

		; Update position
		ch\Position\x# = EntityX(ch\Entity)
		ch\Position\y# = EntityY(ch\Entity)
		ch\Position\z# = EntityZ(ch\Entity)

		; Place mesh
		PositionEntity ch\Mesh, (ch\Position\x#), (ch\Position\y#), (ch\Position\z#)
		If Game\Victory=0 Then RotateEntity ch\Mesh,0,(DeltaYaw#(p\Objects\Entity,ch\Mesh) - 180),0 Else RotateEntity ch\Mesh,0,p\Animation\Direction#-180,0

		; Animate
		If ch\PreviousAnimation<>ch\Animation Then
			Select ch\Animation
				Case 1,2: Animate (ch\Mesh,1,0.255,ch\Animation,10)
				Case 3: Animate (ch\Mesh,1,0.3188,ch\Animation,10)
				Default: Animate (ch\Mesh,1,0.1275,ch\Animation,10)
			End Select
			ch\PreviousAnimation=ch\Animation
		EndIf

		; Decide animation
		If Game\CheeseTimer>0 and (Not(p\Flags\HomingTarget\x#=99999 and p\Flags\HomingTarget\y#=99999 and p\Flags\HomingTarget\z#=99999)) Then
			ch\Animation=3 : ch\Emo\Emotion=CHAOEMO_default
		ElseIf p\Action=ACTION_THROW Then
			ch\Animation=3 : ch\Emo\Emotion=CHAOEMO_angry
		ElseIf p\Action=ACTION_HURT Then
			ch\Animation=5 : ch\Emo\Emotion=CHAOEMO_confused
		ElseIf p\Action=ACTION_DIE Then
			ch\Animation=4 : ch\Emo\Emotion=CHAOEMO_surprised
		Else
			If p\SpeedLength#>0.5 Then
				ch\Animation=2 : ch\Emo\Emotion=CHAOEMO_default
			Else
				ch\Animation=1 : ch\Emo\Emotion=CHAOEMO_default
			EndIf
		EndIf

		; Movement
		If Game\CheeseTimer>0 and (Not(p\Flags\HomingTarget\x#=99999 and p\Flags\HomingTarget\y#=99999 and p\Flags\HomingTarget\z#=99999)) Then
			ParticleTemplate_Call(ch\Particle, PARTICLE_CHAO_CHEESE, ch\Entity)
			PositionEntity(p\Flags\HomingMesh, p\Flags\HomingTarget\x#, p\Flags\HomingTarget\y#+0.3, p\Flags\HomingTarget\z#)
			ex# = p\Flags\HomingTarget\x# - ch\Position\x#
			ey# = (p\Flags\HomingTarget\y#+3) - ch\Position\y#
			ez# = p\Flags\HomingTarget\z# - ch\Position\z#
			AlignToVector(ch\Entity, ex#, ey#, ez#, 2, .925)
			AlignToVector(ch\Mesh, ex#, ey#, ez#, 2, .925)
			TurnEntity ch\Mesh, -90, 0, 0
			If EntityDistance(ch\Entity,p\Flags\HomingMesh)>10 Then
				MoveEntity(ch\Entity, 0, 3.2*d\Delta, 0)
			Else
				MoveEntity(ch\Entity, 0, (EntityDistance(ch\Entity,p\Flags\HomingMesh)/10)*3.2*d\Delta, 0)
			EndIf
			p\Flags\HomingTarget\x#=99999
			p\Flags\HomingTarget\y#=99999
			p\Flags\HomingTarget\z#=99999
			If (Not(ChannelPlaying(ch\Channel_Chao))) Then ch\Channel_Chao=EmitSmartSound(Sound_Chao,ch\Entity)
		Else
			PointEntity ch\Entity, p\Objects\Cheese
			If EntityDistance(p\Objects\Cheese, ch\Entity)>=150 Then
				EntityType(ch\Entity,COLLISION_NONE) : PositionEntity ch\Entity, p\Objects\Position\x#, p\Objects\Position\y#+7, p\Objects\Position\z#
				PositionEntity ch\Mesh, p\Objects\Position\x#, p\Objects\Position\y#+7, p\Objects\Position\z# : EntityType(ch\Entity,COLLISION_OBJECT2_GOTHRU)
			Else
				MoveEntity ch\Entity,0,0,0.15*p\Physics\UNDERWATERTRIGGER#*EntityDistance(p\Objects\Cheese, ch\Entity)*d\Delta
			EndIf
			If (ChannelPlaying(ch\Channel_Chao)) Then StopChannel(ch\Channel_Chao)
		EndIf

	End Function