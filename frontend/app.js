const API_URL = window.location.port === "3000" ? "/api/games" : "http://localhost:8080/api/games";

let currentGameId = null;
let currentTurn = "X";
let isGameOver = true;

const playerXInput = document.getElementById("playerX");
const playerOInput = document.getElementById("playerO");
const statusMessage = document.getElementById("statusMessage");
const cells = document.querySelectorAll(".cell");
const historyList = document.getElementById("historyList");

function startGame() {
  const playerX = playerXInput.value.trim() || "Player X";
  const playerO = playerOInput.value.trim() || "Player O";

  fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      playerX: playerX,
      playerO: playerO
    })
  })
    .then(response => response.json())
    .then(data => {
      currentGameId = data.id;
      currentTurn = data.currentTurn;
      isGameOver = false;
      updateBoard(data);
      loadHistory();
    })
    .catch(error => {
      statusMessage.innerText = "Error starting game. Make sure backend is running.";
    });
}

function handleCellClick(index) {
  if (!currentGameId || isGameOver) {
    return;
  }

  fetch(API_URL + "/" + currentGameId + "/moves", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      player: currentTurn,
      cellIndex: index
    })
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => {
          throw new Error(err.message || "Invalid move");
        });
      }
      return response.json();
    })
    .then(data => {
      updateBoard(data);
      if (data.status !== "IN_PROGRESS") {
        loadHistory();
      }
    })
    .catch(error => {
      alert(error.message);
    });
}

function updateBoard(data) {
  currentTurn = data.currentTurn;
  isGameOver = data.status !== "IN_PROGRESS";

  for (let i = 0; i < 9; i++) {
    const val = data.board[i];
    cells[i].innerText = val;
    cells[i].className = "cell";

    if (val === "X") {
      cells[i].classList.add("x");
    } else if (val === "O") {
      cells[i].classList.add("o");
    }
  }

  if (data.winningLine && data.winningLine.length === 3) {
    for (let i = 0; i < 3; i++) {
      const idx = data.winningLine[i];
      cells[idx].classList.add("winner");
    }
  }

  if (data.status === "X_WON") {
    statusMessage.innerText = data.playerX + " (X) Wins!";
  } else if (data.status === "O_WON") {
    statusMessage.innerText = data.playerO + " (O) Wins!";
  } else if (data.status === "DRAW") {
    statusMessage.innerText = "Game is a Draw!";
  } else {
    const nextPlayer = currentTurn === "X" ? data.playerX : data.playerO;
    statusMessage.innerText = nextPlayer + "'s turn (" + currentTurn + ")";
  }
}

function resetGame() {
  currentGameId = null;
  isGameOver = true;

  for (let i = 0; i < cells.length; i++) {
    cells[i].innerText = "";
    cells[i].className = "cell";
  }

  statusMessage.innerText = "Click Start Game to play";
}

function loadHistory() {
  fetch(API_URL)
    .then(response => response.json())
    .then(games => {
      if (!games || games.length === 0) {
        historyList.innerHTML = "<div>No games found</div>";
        return;
      }

      let html = "";
      for (let i = 0; i < games.length; i++) {
        const g = games[i];
        let result = "In Progress";
        if (g.status === "X_WON") result = g.playerX + " Won";
        if (g.status === "O_WON") result = g.playerO + " Won";
        if (g.status === "DRAW") result = "Draw";

        html += `
          <div class="history-item">
            <span>${g.playerX} vs ${g.playerO}</span>
            <b>${result}</b>
          </div>
        `;
      }
      historyList.innerHTML = html;
    })
    .catch(err => {});
}

document.getElementById("startBtn").addEventListener("click", startGame);
document.getElementById("resetBtn").addEventListener("click", resetGame);
document.getElementById("refreshBtn").addEventListener("click", loadHistory);

loadHistory();
