// script.js

const sidebar = document.getElementById('sidebar');
let isResizing = false;
let lastDownX = 0;
let sidebarWidth = sidebar.offsetWidth;

function initResize(e) {
    isResizing = true;
    lastDownX = e.clientX;
    sidebarWidth = sidebar.offsetWidth;
}

function stopResize() {
    isResizing = false;
}

function resizeSidebar(e) {
    if (!isResizing) return;

    const offset = lastDownX - e.clientX;
    const newSidebarWidth = sidebarWidth + offset;

    if (newSidebarWidth > 100 && newSidebarWidth < 400) {
        sidebar.style.flex = `0 0 ${newSidebarWidth}px`;
    }
}

document.addEventListener('mousemove', resizeSidebar);
document.addEventListener('mouseup', stopResize);
sidebar.addEventListener('mousedown', initResize);
