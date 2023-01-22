using DefectsUI.Db;
using System.Data;

namespace DefectsUI
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void defectsTable_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void BackgroundDataLoader_DoWork(object sender, System.ComponentModel.DoWorkEventArgs e)
        {
            Thread.Sleep(1000);
            var dt = new DataTable();
            var db = new DefectsDb();
            db.Query(dt);
            DefectsTable.DataSource = dt;
            if (DefectsTable.ColumnCount > 0)
            {
                DefectsTable.AutoResizeColumn(DefectsTable.Columns.Count - 1, DataGridViewAutoSizeColumnMode.Fill);
            }
            
        }

        private void BackgroundDataLoader_RunWorkerCompleted(object sender, System.ComponentModel.RunWorkerCompletedEventArgs e)
        {
            LoadingBar.Visible = false;
            LoadingLabel.Visible = false;
            DefectsTable.Visible = true;
        }
    }
}