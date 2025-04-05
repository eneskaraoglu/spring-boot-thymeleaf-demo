// Ana JavaScript dosyası

document.addEventListener('DOMContentLoaded', function() {
    // Sayfa yüklendiğinde çalışacak kodlar
    console.log('Thymeleaf Demo sayfası yüklendi');
    
    // Aktif menü öğesini vurgulama
    highlightActiveMenuItem();
    
    // Form doğrulama
    setupFormValidation();
});

// Aktif menü öğesini vurgulama
function highlightActiveMenuItem() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');
    
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPath || (href !== '/' && currentPath.startsWith(href))) {
            link.classList.add('active');
        }
    });
}

// Basit form doğrulama
function setupFormValidation() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            let isValid = true;
            
            // Gerekli alanları kontrol et
            const requiredFields = form.querySelectorAll('[required]');
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    field.classList.add('is-invalid');
                } else {
                    field.classList.remove('is-invalid');
                }
            });
            
            // E-posta alanını kontrol et
            const emailFields = form.querySelectorAll('input[type="email"]');
            emailFields.forEach(field => {
                if (field.value && !validateEmail(field.value)) {
                    isValid = false;
                    field.classList.add('is-invalid');
                }
            });
            
            if (!isValid) {
                event.preventDefault();
                alert('Lütfen form alanlarını doğru şekilde doldurun.');
            }
        });
    });
}

// E-posta doğrulama
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

// Tablo sıralama için yardımcı fonksiyon
function sortTable(tableId, columnIndex) {
    const table = document.getElementById(tableId);
    if (!table) return;
    
    let switching = true;
    let dir = 'asc';
    let switchcount = 0;
    
    while (switching) {
        switching = false;
        const rows = table.rows;
        
        for (let i = 1; i < rows.length - 1; i++) {
            let shouldSwitch = false;
            const x = rows[i].getElementsByTagName('TD')[columnIndex];
            const y = rows[i + 1].getElementsByTagName('TD')[columnIndex];
            
            if (dir === 'asc') {
                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            } else if (dir === 'desc') {
                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount++;
        } else {
            if (switchcount === 0 && dir === 'asc') {
                dir = 'desc';
                switching = true;
            }
        }
    }
}
