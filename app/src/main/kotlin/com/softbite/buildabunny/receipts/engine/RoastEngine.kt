package com.softbite.buildabunny.receipts.engine

import com.softbite.buildabunny.receipts.data.model.MoodTag

object RoastEngine {

    data class RoastResult(
        val roast: String,
        val diagnosis: String,
        val realityCheck: String,
    )

    fun generate(text: String, tags: List<MoodTag>): RoastResult {
        val roast = buildRoast(text, tags)
        val diagnosis = buildDiagnosis(text, tags)
        val realityCheck = buildRealityCheck(tags)
        return RoastResult(roast, diagnosis, realityCheck)
    }

    private fun buildRoast(text: String, tags: List<MoodTag>): String {
        val lower = text.lowercase()
        return when {
            lower.hasAny("gaming", "controller", "game", "xbox", "playstation", "ps5", "nintendo") ->
                "Controller Paralysis: a rare condition where the thumbs work perfectly but the ears stop functioning entirely. Misty has taken note."

            lower.hasAny("forgot", "forget", "doesn't remember", "can't remember", "no memory") ->
                "Ah yes. The Selective Memory Download is running. Funny how it only glitches for things that require effort."

            lower.hasAny("dishes", "cleaning", "laundry", "mess", "clean up", "tidied", "vacuum") ->
                "Domestic Blindness confirmed. The mess was visible. The inconvenience was not. Misty has filed a report."

            lower.hasAny("sorry but", "apologize but", "i'm sorry however", "sorry, but") ->
                "A 'sorry but' is just a criticism wearing a sorry costume. Misty sees through the disguise."

            lower.hasAny("crazy", "paranoid", "overreacting", "too sensitive", "dramatic", "being dramatic") ->
                "Someone called you crazy for having a valid reaction. Classic. Misty is updating the database."

            lower.hasAny("silent", "silence", "ignoring", "no response", "read it", "left on read") ->
                "Silence as punishment. One of the oldest tricks in the emotional manipulation playbook. Vintage behavior."

            lower.hasAny("never said", "didn't say that", "never asked", "you assumed", "i never") ->
                "The 'I Never Technically Said That' defense. Very popular. Very exhausting. Misty is unimpressed."

            lower.hasAny("their fault", "blame", "your fault", "blaming me") ->
                "Accountability went on vacation and didn't leave a return date. Misty is holding the fort."

            lower.hasAny("fine", "i'm fine", "it's fine", "whatever") ->
                "'Fine' carrying the emotional weight of a thousand unspoken grievances. Misty hears the subtext."

            lower.hasAny("promised", "said they would", "said they'd", "was supposed to") ->
                "The promise was made. The promise has aged poorly. The promise is now a receipt."

            MoodTag.RED_FLAG in tags -> redFlagRoasts.random()
            MoodTag.CLOWN in tags -> clownRoasts.random()
            MoodTag.FUMING in tags -> fumingRoasts.random()
            MoodTag.DEAD in tags -> deadRoasts.random()
            MoodTag.ROLLING in tags -> rollingRoasts.random()
            MoodTag.CRYING in tags -> cryingRoasts.random()
            MoodTag.CHAOS in tags -> chaosRoasts.random()
            MoodTag.SIDE_EYE in tags -> sideEyeRoasts.random()
            else -> genericRoasts.random()
        }
    }

    private fun buildDiagnosis(text: String, tags: List<MoodTag>): String {
        val lower = text.lowercase()
        return when {
            lower.hasAny("gaming", "controller", "game") -> "Controller Paralysis™"
            lower.hasAny("forgot", "forget", "remember") -> "Selective Amnesia Syndrome™"
            lower.hasAny("dishes", "cleaning", "mess", "laundry") -> "Domestic Blindness Disorder™"
            lower.hasAny("crazy", "paranoid", "overreacting", "dramatic") -> "Weaponized Gaslight Exposure™"
            lower.hasAny("silent", "silence", "ignoring", "left on read") -> "Strategic Silence Deployment™"
            lower.hasAny("sorry but", "sorry, but", "apologize but") -> "Conditional Apology Syndrome™"
            lower.hasAny("promised", "said they would", "supposed to") -> "Chronic Promise Inflation™"
            lower.hasAny("fine", "i'm fine", "it's fine") -> "Suppressed Grievance Accumulation™"
            MoodTag.RED_FLAG in tags -> "Advanced Red Flag Accumulation™"
            MoodTag.CHAOS in tags -> "Unregulated Chaos Emission™"
            MoodTag.CLOWN in tags -> "Involuntary Clownery Exposure™"
            MoodTag.DEAD in tags -> "Emotional Fatality Syndrome™"
            MoodTag.ROLLING in tags -> "Repetitive Explanation Exhaustion™"
            MoodTag.CRYING in tags -> "Deferred Feelings with Interest™"
            MoodTag.FUMING in tags -> "Righteous Indignation Overload™"
            MoodTag.SIDE_EYE in tags -> "Pattern Recognition Hyperactivity™"
            else -> "General Unhinged Behavior Disorder™"
        }
    }

    private fun buildRealityCheck(tags: List<MoodTag>): String = when {
        MoodTag.CLOWN in tags ->
            "You're not the clown. You're the one watching the circus without having signed up for a ticket. Your expectations of basic human cooperation are completely valid."

        MoodTag.RED_FLAG in tags ->
            "No, you are not tripping. The flag was red. It was large. It was waving directly in your face. Your pattern recognition is working perfectly."

        MoodTag.CHAOS in tags ->
            "The chaos was real. Your reaction to it is normal. Anyone in this situation would feel the same way. You're not too sensitive — the situation was genuinely unhinged."

        MoodTag.FUMING in tags ->
            "Your anger is data, not a flaw. Something happened that crossed a line. You noticed. That's not overreacting — that's self-awareness working correctly."

        MoodTag.DEAD in tags ->
            "Exhaustion from carrying an unfair load is valid. You didn't sign up to be everyone's support system while running on empty. This is not dramatic. This is tired."

        MoodTag.SIDE_EYE in tags ->
            "The side-eye was warranted. Your suspicion has logical roots. You're not paranoid — you're pattern-matching from experience, which is a feature not a bug."

        MoodTag.ROLLING in tags ->
            "Repeating yourself to someone who doesn't listen is genuinely maddening. You're not unreasonable for wanting to be heard once. Once is not a lot to ask."

        MoodTag.CRYING in tags ->
            "Crying about this is a completely proportionate response. The situation earned those tears. You don't need to apologize for having feelings about something that affected you."

        else ->
            "Your feelings are real. The situation happened. You are not making this up, and you are not overreacting. Misty has reviewed the case and confirms: you are not tripping."
    }

    // ─── Roast libraries ──────────────────────────────────────────────────────

    private val redFlagRoasts = listOf(
        "The flags are so red even colorblind people are concerned. Misty has counted at least three.",
        "Red flag. Red flag. Red flag. At this point it's a whole parade and you've been standing on the curb watching it.",
        "That's a flag. That's definitely a flag. Misty would like to file this under 'Patterns Worth Acknowledging'.",
        "Your GPS has been saying 'recalculating' for months. The destination is not getting better.",
    )

    private val clownRoasts = listOf(
        "You've been doing the emotional labor AND providing the entertainment. The clown car has left the building.",
        "Honey. You've been starring in your own circus and didn't even charge admission.",
        "The clown makeup was applied without your consent. Misty recommends leaving the big top.",
        "At least a real clown gets a salary. You're freelancing in this circus for free.",
    )

    private val fumingRoasts = listOf(
        "Your frustration is valid. Your blood pressure is not. Misty is concerned for both.",
        "The steam coming off this is visible from space. Misty applauds the restraint you've shown.",
        "Fuming is the correct response. Absolutely the correct response. Let the record show.",
        "You're not overreacting. The situation is under-respecting your patience.",
    )

    private val deadRoasts = listOf(
        "Deceased. Gone. Left the chat. And yet somehow still expected to do the dishes.",
        "The spirit has left the body but the obligations remain. Peak modern experience.",
        "Dead inside, alive outside, still expected to be pleasant about it. Truly impressive.",
        "💀 Misty has filed a wellness check. The results are: done. Completely and utterly done.",
    )

    private val rollingRoasts = listOf(
        "Your eyes have officially logged more miles than a long-haul driver. Rest is warranted.",
        "The eye roll is a complete sentence and you've written a novel with it today.",
        "Repetition is not a teaching strategy if the student refuses to learn. You know this.",
        "The amount of times you've explained this could fill a curriculum. And yet. Here we are.",
    )

    private val cryingRoasts = listOf(
        "Crying in the group chat. Crying in the car. Crying at checkout. A truly immersive experience.",
        "The tears are a valid receipt. Misty accepts them as primary source evidence.",
        "You've been so strong for so long. This is just deferred feelings arriving with interest.",
        "The crying is data. Something crossed a threshold. The threshold was a reasonable one.",
    )

    private val chaosRoasts = listOf(
        "Chaos level: 'why is the WiFi password written in invisible ink and why am I the only one who cares'.",
        "This is the controlled chaos zone. Except it wasn't controlled. And the zone is on fire.",
        "Misty has reviewed the situation and classified it as: genuinely unhinged. You're not imagining it.",
        "The chaos was not your fault. You just happened to be the person paying attention to it.",
    )

    private val sideEyeRoasts = listOf(
        "The side-eye you're giving deserves its own Emmy. Supporting role at minimum.",
        "That look contains multitudes. Misty recognizes it as the universal symbol for 'are you serious right now'.",
        "Side-eye activated. Pattern recognized. Misty has updated the file. The file is thick.",
        "Your face has said everything your words were too polite to say. Respect.",
    )

    private val genericRoasts = listOf(
        "Filed, dated, and notarized. The receipt has been received. Misty is not surprised.",
        "Another log for the archives. The archives are getting thick. Misty notices.",
        "The behavior has been documented. The documentation is growing. The growth is a data point.",
        "Misty has reviewed this and has chosen to stare at you with quiet, knowing energy. You know what that means.",
        "This is a lot. The 'a lot' is not your fault. Misty wants you to know that.",
    )

    private fun String.hasAny(vararg keywords: String): Boolean =
        keywords.any { this.contains(it, ignoreCase = true) }
}
