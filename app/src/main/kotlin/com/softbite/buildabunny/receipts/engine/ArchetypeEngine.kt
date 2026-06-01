package com.softbite.buildabunny.receipts.engine

import com.softbite.buildabunny.receipts.data.model.Archetype
import com.softbite.buildabunny.receipts.data.model.MoodTag
import com.softbite.buildabunny.receipts.data.model.Receipt

object ArchetypeEngine {

    private val library: List<Archetype> = listOf(
        // Level 1: Everyday Mindfucks
        Archetype(
            id = "selective_amnesia",
            name = "The Selective Amnesia Unit",
            level = 1,
            emoji = "🧠",
            tagline = "Remembers your coffee order from 2019. Forgot the thing you said five minutes ago.",
            description = "Exhibits remarkably precise memory for unimportant details while consistently failing to retain anything that requires follow-through or accountability.",
            antidote = "Try text-based confirmation for important things. You can't gaslight a screenshot.",
            antidoteName = "The Paper Trail",
        ),
        Archetype(
            id = "conditional_helper",
            name = "The Conditional Helper",
            level = 1,
            emoji = "🤝",
            tagline = "Happy to help. Conditions apply. Fine print very long.",
            description = "Provides support exclusively when it's convenient, when they're in a good mood, or when it creates an opportunity to be recognized for it later.",
            antidote = "Notice when help is offered proactively vs. only when asked. The pattern tells the story.",
            antidoteName = "The Baseline Check",
        ),
        Archetype(
            id = "deflector",
            name = "The Deflector",
            level = 1,
            emoji = "🏓",
            tagline = "You: I feel hurt. Them: Well I feel hurt MORE.",
            description = "Every conversation about your feelings becomes a conversation about their feelings. Your concern gets returned to sender with extra postage.",
            antidote = "Name the pattern out loud: 'I'm noticing we moved away from what I was saying.' Deflectors hate a mirror.",
            antidoteName = "The Pattern Name",
        ),
        // Level 2: Advanced Patterns
        Archetype(
            id = "emotional_accountant",
            name = "The Emotional Accountant",
            level = 2,
            emoji = "🧾",
            tagline = "Keeping score since forever. Invoicing at the worst possible moment.",
            description = "Tracks every favor, every concession, every kind thing they've ever done. Presents the bill at the most strategic and destabilizing moments.",
            antidote = "Relationships aren't transactions. If the ledger is always unbalanced in their favor, that's not accounting — it's control.",
            antidoteName = "The Equity Audit",
        ),
        Archetype(
            id = "moving_goalpost",
            name = "The Moving Goalpost",
            level = 2,
            emoji = "🥅",
            tagline = "You meet the standard. The standard moves. Always.",
            description = "Whatever you do is never quite enough. Once you achieve what was asked, the criteria shift. Perfection is not the goal — your perpetual inadequacy is.",
            antidote = "Stop running toward the goalpost. It will keep moving. Decide if you want to play this game.",
            antidoteName = "The Boundary Setter",
        ),
        Archetype(
            id = "strategic_victim",
            name = "The Strategic Victim",
            level = 2,
            emoji = "🎭",
            tagline = "Somehow always the most wronged person in the room.",
            description = "Whenever accountability approaches, they become the victim of the situation. Your valid concern becomes an attack they need to recover from.",
            antidote = "Hold the line gently: 'I hear you're upset. I still want to talk about what happened.'",
            antidoteName = "The Reality Anchor",
        ),
        // Level 3: Seriously Concerning
        Archetype(
            id = "reality_distorter",
            name = "The Reality Distorter",
            level = 3,
            emoji = "🌀",
            tagline = "You were there. You saw it. Apparently neither of those things happened.",
            description = "Consistently rewrites events after the fact. Your clear memory of what happened is presented as confusion or misinterpretation.",
            antidote = "Document. Document. Document. Your memory is accurate — and receipts don't lie.",
            antidoteName = "The Receipt Keeper",
        ),
        Archetype(
            id = "silence_deployer",
            name = "The Weaponized Silence Expert",
            level = 3,
            emoji = "🔇",
            tagline = "The silent treatment, but make it tactical.",
            description = "Uses silence not to process, but to punish. The withdrawal is calibrated to create maximum anxiety and compliance.",
            antidote = "Silence used as punishment is emotional coercion. You don't have to fill that silence or earn your way back into conversation.",
            antidoteName = "The Silence Reader",
        ),
        Archetype(
            id = "intensity_oscillator",
            name = "The Intensity Oscillator",
            level = 3,
            emoji = "💣",
            tagline = "The highest highs. The lowest lows. Rinse. Repeat.",
            description = "Alternates between overwhelming affection and complete withdrawal. The unpredictability keeps you off-balance and attached.",
            antidote = "Consistency is the metric, not intensity. Ask: what does the average day look like, not the best or worst.",
            antidoteName = "The Consistency Check",
        ),
        // Level 4: Elite Tier
        Archetype(
            id = "alternate_reality",
            name = "The Alternate Reality Architect",
            level = 4,
            emoji = "🏗️",
            tagline = "They built a narrative where they're always right. You weren't invited to the blueprint meeting.",
            description = "Constructs an elaborate alternative version of events in which their behavior is always justified and your perspective is always flawed. The alternate reality is load-bearing — it cannot be questioned.",
            antidote = "You cannot win an argument inside someone else's constructed reality. External support (trusted friends, a therapist) helps you keep your grip on what actually happened.",
            antidoteName = "The External Validator",
        ),
    )

    fun detectArchetypes(receipts: List<Receipt>): List<Archetype> {
        if (receipts.size < 10) return emptyList()

        val detected = mutableSetOf<String>()

        fun matchesInReceipts(vararg keywords: String): Int =
            receipts.count { r -> keywords.any { r.text.contains(it, ignoreCase = true) } }

        fun tagCount(tag: MoodTag): Int = receipts.count { tag in it.moodTags }

        if (matchesInReceipts("forgot", "forget", "doesn't remember", "can't remember") >= 2)
            detected += "selective_amnesia"

        if (matchesInReceipts("only helps when", "never helps", "won't help", "didn't help", "refused to help") >= 2)
            detected += "conditional_helper"

        if (matchesInReceipts("made it about", "turned it around", "back to them", "about themselves") >= 2
            || (tagCount(MoodTag.ROLLING) >= 3 && tagCount(MoodTag.FUMING) >= 2))
            detected += "deflector"

        if (matchesInReceipts("keep score", "reminded me", "brought up again", "you owe", "never forget") >= 2)
            detected += "emotional_accountant"

        if (matchesInReceipts("not enough", "still not", "never good enough", "could have done better") >= 2)
            detected += "moving_goalpost"

        if (matchesInReceipts("always the victim", "made me feel bad", "turned it on me") >= 2)
            detected += "strategic_victim"

        if (matchesInReceipts("crazy", "paranoid", "imagining", "didn't happen", "never said") >= 2)
            detected += "reality_distorter"

        if (matchesInReceipts("silent", "silence", "ignoring", "stopped talking", "left on read") >= 2)
            detected += "silence_deployer"

        if (tagCount(MoodTag.CHAOS) >= 3)
            detected += "intensity_oscillator"

        if (tagCount(MoodTag.RED_FLAG) >= 4)
            detected += "alternate_reality"

        if (detected.isEmpty())
            detected += "selective_amnesia"

        return library
            .filter { it.id in detected }
            .sortedByDescending { it.level }
    }

    fun dominantTags(receipts: List<Receipt>): List<Pair<MoodTag, Int>> =
        MoodTag.entries
            .map { tag -> tag to receipts.count { tag in it.moodTags } }
            .filter { (_, count) -> count > 0 }
            .sortedByDescending { (_, count) -> count }
            .take(4)
}
