# 🪴 HabitOS

HabitOS is a cool application (so cool 😎) which helps user to track all the habits he wants to track and provides weekly reports of progress via email.

---

🧠 Habit Tracker – Project Blueprint

I'll build it in phases so I don’t get overwhelmed 👀

Tech stack:

- Backend: Spring Boot

- Frontend: Angular

- DB: PostgreSQL

- Auth: JWT

- Scheduling: Spring Scheduler

- Email: Spring Mail

- Caching: Redis (later)

- Docker: Later (final phase)

---

📌 Core Features (Backend-first)

🧪 Habits I Can Track

I’ll be able to track habits like:

🏃 Exercise

💻 GitHub contribution

🧠 Learning (DSA / System Design / Reading)

🧑‍💼 Work hours

🍭 Sugar-free day

😴 Sleep target

📖 Reading

🧘 Meditation ... and many more


Each habit will support:

- Daily / Weekly frequency

- Streak tracking

- Skip rules (holidays, weekends)

- Notes (optional)

---

🔁 Weekly Email Summary (Key Feature)

Every week:

- % consistency per habit

- Current streak

- Longest streak

- Missed days

- Motivation message 😄

---

🧠 Backend Modules Breakdown

✅ Phase 1: Core Backend

- Spring Boot project

- User registration & login (JWT)

- Habit CRUD

- Habit entry logging

- Skip rule support

- Streak calculation

⏰ Phase 2: Scheduler + Email

- Weekly cron job

- Aggregate habit stats

- Send email

⚡ Phase 3: Caching (Redis)

- Cache habit stats

- Cache streaks

- Evict cache on new entry

🧪 Phase 4: Testing

- Unit tests (Service layer)

- Integration tests (Postgres Testcontainers)

🐳 Phase 5: Dockerization

- Backend Dockerfile

- PostgreSQL container

- docker-compose

🌐 Phase 6: Angular Frontend

- Dashboard

- Habit calendar

- Streak visualization

- Weekly statistics

---

## 🧠 Features and Logic

🔁 Weekly Email Summary (Key Feature)

Every week:

% consistency per habit

Current streak

Longest streak

Missed days

Motivation message 😄

---

🧠 Habit Concepts

Each habit has:

Name (Exercise, GitHub, Reading)

Frequency (DAILY / WEEKLY)

Target (e.g. 5 days/week)

Active flag

Owner (User)

🧠 Core Rules

A habit can have only ONE entry per date

Status is explicitly logged (DONE / SKIPPED)

MISSED is inferred (no entry + not skipped)

Entries are immutable for date (no duplicates)

📌 Unique constraint prevents duplicate logs per day
📌 value allows future habits like:

Work hours

Reading time

Pages read

📌  Streak Calculation

We will:

Compute current streak

Compute longest streak

Respect SKIPPED days

Store or cache streaks

🧠 Streak Rules

✅ Definitions

DONE → counts toward streak

SKIPPED → does NOT break streak

NO ENTRY → breaks streak

Streak is date-based, not entry-count-based


🧠 Skip rules & holidays

This step ensures:

Sundays don’t break work/gym streaks

Public holidays are respected

Users can define custom skip rules

Streak logic becomes real-world accurate


🧠  Weekly Email Reports

Every week (say Monday 9 AM):

  - Generate weekly analytics for each user

  - Build a clean summary email

  - Send it automatically

  - Reuse your existing analytics logic (VERY important)


🧠 Caching

Use Redis caching for expensive computations

Implement TTL-based cache

Handle cache invalidation correctly

Avoid stale data bugs

---

Areas of improvement

- Can we send messages also along with mails?

- 

---
## 💡 Key Findings

  - When using Redis with JSON serialization, cached objects must have either a default constructor or a Jackson creator, otherwise deserialization fails on cache hits.
  - Habit IDs are the canonical identifiers. For usability, we also expose name-based lookup APIs that internally resolve IDs, while keeping core logic ID-driven.

---
## 📜 API Documentation
🔐 Authentication APIs

1️⃣ Register User

POST:  /api/auth/register

Request Body

{
  "name": "Test",
  "email": "test@example.com",
  "password": "test123"
}

Response

{
  "token": "jwt-token"
}

---


2️⃣ Login User

POST:  /api/auth/login

Request Body

{
  "email": "test@test.com",
  "password": "test1234"
}

Response

{
  "token": "jwt-token"
}

---
🧠 Habit Management APIs

3️⃣ Create Habit

POST:  /api/habits

Request Body

{
  "name": "Exercise",
  "description": "Daily workout",
  "frequency": "DAILY",
  "targetCount": 1
}

---

4️⃣ Get All Habits

GET:  /api/habits

Response

[
  {
    "id": 1,
    "name": "Exercise",
    "frequency": "DAILY",
    "active": true
  }
]

---

5️⃣ Update Habit

PUT:  /api/habits/{habitId}

Request Body

{
  "name": "Workout",
  "description": "Gym workout",
  "targetCount": 1
}

---

6️⃣ Deactivate Habit

DELETE:  /api/habits/{habitId}

📌 Habit is soft-deleted (marked inactive).

---

📝 Habit Entry APIs

7️⃣ Mark Habit as DONE

POST:  /api/habits/{habitId}/entries

Request Body

{
  "date": "2026-01-10",
  "status": "DONE"
}

---

8️⃣ Skip Habit

POST:  /api/habits/{habitId}/entries

Request Body

{
  "date": "2026-01-10",
  "status": "SKIPPED"
}

---

🚫 Skip Rules & Holidays

9️⃣ Add Skip Rule (Weekly)

POST:  /api/habits/{habitId}/skip-rules

Request Body

{
  "dayOfWeek": "SUNDAY"
}

---

🔟 Add Holiday (Specific Date)

POST:  /api/habits/{habitId}/skip-rules/date

Request Body

{
  "date": "2026-01-26"
}

---

🔥 Streak APIs

1️⃣1️⃣ Get Habit Streak

GET:  /api/streaks/{habitId}

Response

{
  "habitId": 1,
  "currentStreak": 5,
  "longestStreak": 12
}

---

📊 Progress APIs

1️⃣2️⃣ Get Habit Progress (Timeline)

GET:  /api/habits/{habitId}/progress

Optional query params:

?from=2026-01-01&to=2026-01-31

Response

{
  "habitId": 1,
  "habitName": "Exercise",
  "startDate": "2026-01-01",
  "endDate": "2026-01-10",
  "progress": [
    { "date": "2026-01-01", "status": "DONE" },
    { "date": "2026-01-02", "status": "MISSED" },
    { "date": "2026-01-03", "status": "SKIP_RULE"}
  ]
}

---

1️⃣3️⃣ Get Habit Progress by Name

GET:  /api/habits/progress?name=Exercise

📌 Internally resolves habit ID.

---

1️⃣4️⃣ Bulk Progress (All Habits)

GET:  /api/habits/progress/bulk

Optional:

?from=2026-01-01&to=2026-01-31

Response

{
  "startDate": "2026-01-01",
  "endDate": "2026-01-10",
  "habits": [
    {
      "habitId": 1,
      "habitName": "Exercise",
      "progress": [...]
    }
  ]
}

---

📈 Analytics APIs

1️⃣5️⃣ Weekly Analytics

GET:  /api/analytics/weekly

Response

{
  "totalHabits": 3,
  "completed": 18,
  "skipped": 2,
  "missed": 1,
  "consistencyPercentage": 85
}

---

1️⃣6️⃣ Monthly Analytics

GET:  /api/analytics/monthly

---

📧 Email Reports

1️⃣7️⃣ Weekly Email Summary (Auto)

📌 Sent every week via scheduler:

Consistency

Streaks

Completion %

(No manual API trigger)

---

⚠️ Error Handling

All errors follow a standard format:

{
  "timestamp": "2026-01-19T00:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid date range",
  "path": "/api/habits/1/progress"
}

---
## ✍️ Author

Aditya Upadhyaya

GitHub: [123-Aditya](https://github.com/123-Aditya)
