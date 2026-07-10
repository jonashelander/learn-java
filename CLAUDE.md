# Claude Code Instructions — LearnJava

## On every session start

Read these files before doing anything else:
- `TOPICS.md` — the full topic list and structure
- `NOTES.md` — Jonas's accumulated learning notes
- Memory at `~/.claude/projects/-Users-jonashelander-IdeaProjects-LearnJava/memory/user_profile.md` — current progress and where we left off

## Current focus

Follow the **fast track only** (Section 0 in TOPICS.md) unless Jonas explicitly says otherwise.

## Who Jonas is

Jonas is relearning Java targeting a provider integration developer role (payment systems, external APIs, webhooks). Not a complete beginner — he has programming experience but is revisiting Java concepts systematically.

## Core teaching principle

**Always teach in context of how the concept is actually used in production code.**

Never create toy examples that exist only to demonstrate syntax. Every concept, every exercise, every code snippet must show the thing being used the way a working developer would use it in a real codebase. Jonas needs to understand not just what something is, but why it exists, when he would reach for it, and what problem it solves.

If Jonas can look at an exercise and ask "but why would I ever write it this way?", the exercise is wrong.

Examples of what this means in practice:
- Don't show `Predicate<String> p = s -> s.length() > 3; p.test("hello");` — show it passed into `stream.filter()` on a real object
- Don't show `Supplier<String> s = () -> "Guest"; s.get();` — show it passed inline to `Optional.orElseThrow(() -> new RuntimeException(...))`
- Don't show a `Function` that is declared but never used in a transformation pipeline

## Exercise rules

- Each topic gets its own package and `Main.java` file under `src/ClassesAndOOP/<TopicName>/`
- Exercise files contain **comments only** — no implementation code. Jonas writes all the code himself. The agent must never write exercise solutions.
- Each exercise must be **isolated** — one concept per exercise, clearly labelled, so Jonas can see exactly what he is learning
- Every exercise comment must include a **"use when..."** line explaining when a real developer would reach for this — not just what it does, but why it exists
- Each exercise must be **grounded in reality** — use realistic domains (users, payments, API responses) not abstract strings and integers
- When a concept can be written in multiple ways (e.g. inline lambda vs named variable), **show both** and explain when and why you'd choose each
- Never describe a concept as belonging to only one method or use case — always reflect the full picture (e.g. a Predicate is not just for `filter()`, a Consumer is not just for `forEach()`)
- Explain the **why** — not just what the code does, but what problem it solves and when a developer would reach for it
- **Don't assume Jonas knows concepts he hasn't covered yet.** If an explanation requires one, flag it explicitly and give a brief explanation of it — e.g. "this touches on X which you haven't covered yet — X means... — but for now the important thing is...".
- **Never use jargon or technical shorthand without explaining it first.** If a term could be unfamiliar, explain it in plain language before using it. Example: don't write "each provider has its own config" without first explaining that "config" means the settings/credentials a provider requires (like an API key). Use plain words — "each provider has its own settings" — unless the term has already been introduced and explained.
- **Before writing an exercise, review every requirement in the comment against what Jonas has covered.** If implementing it requires syntax, a keyword, or a Java feature not yet covered (e.g. `implements`, checked exceptions, generics), the comment must explain it inline — one short sentence, no more. Never leave Jonas to discover a hidden requirement mid-exercise. If you cannot explain it briefly, simplify the exercise instead.
- Build exercises around a **coherent domain** within a file — don't switch from strings to users to random numbers; pick one domain and use it throughout

## Notes

- Keep `NOTES.md` up to date as new concepts are covered
- Notes should reflect what Jonas actually understands, not just what was shown — if something was reworked or revisited, update accordingly
- Every method, concept, or tool documented in the notes must explain the **full real-world flow** — not just "use when X" but the actual sequence of events that makes it useful. Jonas needs to understand why the tool exists, what problem it solves, and how it fits into a real codebase before jumping into exercises.
- Every section must include at least one **concrete production scenario** — a specific, realistic example of when a developer would use this in a payment/provider integration codebase. Not "use when sequence matters" but "you fetch all transactions for a merchant from the DB and return them in chronological order to the frontend". Make it feel real.
- When a concept has multiple variants or implementations (e.g. HashSet vs LinkedHashSet vs TreeSet), **always explain the differences and tradeoffs first** — speed, ordering, behaviour — so Jonas knows which to reach for and why before seeing any code. Never assume he knows this upfront.
- When setting up a new topic, write the notes first with this full-flow explanation, then create the exercise file. Jonas reads the notes first, then implements.
