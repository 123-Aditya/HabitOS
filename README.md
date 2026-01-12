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

🧠 Habit Concepts

Each habit has:

Name (Exercise, GitHub, Reading)

Frequency (DAILY / WEEKLY)

Target (e.g. 5 days/week)

Active flag

Owner (User)


---

Areas of improvement

- Can we send messages also along with mails?

- 

---
## 💡 Author

Aditya Upadhyaya

GitHub: [123-Aditya](https://github.com/123-Aditya)
