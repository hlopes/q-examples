const { createApp, ref, onMounted } = Vue;

const app = createApp({
    setup() {
        const revenues = ref([]);
        const filters = ref({
            'global': { value: null, matchMode: 'contains' },
        });

        onMounted(async () => {
            const response = await fetch('/api/revenues');
            revenues.value = await response.json();
        });

        const formatCurrency = (value) => {
            return new Intl.NumberFormat('en-US', {
                style: 'currency',
                currency: 'USD',
            }).format(value);
        };

        return { revenues, filters, formatCurrency };
    }
});

app.use(PrimeVue.Config, {
    theme: {
        preset: PrimeUIX.Themes.Aura
    }
});
app.component('p-datatable', PrimeVue.DataTable);
app.component('p-column', PrimeVue.Column);
app.component('p-inputtext', PrimeVue.InputText);
app.mount('#app');