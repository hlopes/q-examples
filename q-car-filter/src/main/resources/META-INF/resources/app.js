const { createApp, ref, computed, onMounted } = Vue;

        const app = createApp({
            setup() {
                // Reactive state variables
                const cars = ref([]);
                const loading = ref(false);
                const filterOptions = ref({ brands: [], dealerships: [], colors: [], features: [] });
                const filters = ref({});
                const pagination = ref({ page: 0, rows: 5, totalRecords: 0 });

                // Fetch data to populate filter dropdowns
                const loadFilterOptions = async () => {
                    const response = await fetch('api/cars/filter-options');
                    filterOptions.value = await response.json();
                };

                // Main search function
                const searchCars = async () => {
                    loading.value = true;

                    // Convert brand and dealership names back to IDs for backend
                    const transformedFilters = { ...filters.value };

                    // Convert brand names to IDs
                    if (transformedFilters.brandNames && transformedFilters.brandNames.length > 0) {
                        transformedFilters.brandIds = transformedFilters.brandNames.map(name => {
                            const brand = filterOptions.value.brands.find(b => b.name === name);
                            return brand ? brand.id : null;
                        }).filter(id => id !== null);
                        delete transformedFilters.brandNames;
                    }

                    // Convert dealership names to IDs
                    if (transformedFilters.dealershipNames && transformedFilters.dealershipNames.length > 0) {
                        transformedFilters.dealershipIds = transformedFilters.dealershipNames.map(nameCity => {
                            const dealership = filterOptions.value.dealerships.find(d => `${d.name} - ${d.city}` === nameCity);
                            return dealership ? dealership.id : null;
                        }).filter(id => id !== null);
                        delete transformedFilters.dealershipNames;
                    }

                    // Remove null/empty properties from the filter object before sending
                    const activeFilters = Object.entries(transformedFilters)
                        .filter(([key, value]) => value !== null && value !== '' && (!Array.isArray(value) || value.length > 0))
                        .reduce((obj, [key, value]) => ({ ...obj, [key]: value }), {});

                    const response = await fetch(`api/cars/search?page=${pagination.value.page}&size=${pagination.value.rows}`, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(activeFilters)
                    });

                    cars.value = await response.json();
                    pagination.value.totalRecords = parseInt(response.headers.get('X-Total-Count') || 0);
                    loading.value = false;
                };

                // Handle pagination changes
                const onPageChange = (event) => {
                    pagination.value.page = event.page;
                    pagination.value.rows = event.rows;
                    searchCars();
                };

                // Reset all filters and search again
                const resetFilters = () => {
                    filters.value = {};
                    pagination.value.page = 0; // Go back to the first page
                    searchCars();
                };

                // Helper to format currency
                const formatCurrency = (value) => {
                    if (typeof value !== 'number') return '';
                    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(value);
                };

                // Computed properties to create simple string arrays like Colors
                const brandNames = computed(() => {
                    return filterOptions.value.brands.map(brand => brand.name);
                });

                const dealershipNames = computed(() => {
                    return filterOptions.value.dealerships.map(dealership => `${dealership.name} - ${dealership.city}`);
                });





                // Load initial data when the component is mounted
                onMounted(() => {
                    loadFilterOptions();
                    searchCars();
                });

                return {
                    cars,
                    loading,
                    filters,
                    filterOptions,
                    brandNames,
                    dealershipNames,
                    pagination,
                    searchCars,
                    resetFilters,
                    onPageChange,
                    formatCurrency
                };
            }
        });

        // Register PrimeVue and its components
        app.use(PrimeVue.Config, {
            theme: { preset: PrimeUIX.Themes.Aura }
        });

        app.component('p-datatable', PrimeVue.DataTable);
        app.component('p-column', PrimeVue.Column);
        app.component('p-paginator', PrimeVue.Paginator);
        app.component('p-multiselect', PrimeVue.MultiSelect);
        app.component('p-inputnumber', PrimeVue.InputNumber);
        app.component('p-button', PrimeVue.Button);
        app.mount('#app');
